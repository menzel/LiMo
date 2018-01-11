package ij.plugin.frame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.measure.*;

/** Adjusts the lower and upper threshold levels of the active image. This
	class is multi-threaded to provide a more responsive user interface. */
public class ThresholdAdjuster extends PlugInFrame implements PlugIn, Measurements,
	Runnable, ActionListener, AdjustmentListener, ItemListener {

	public static final String LOC_KEY = "threshold.loc";
	public static final String MODE_KEY = "threshold.mode";
	public static final String DARK_BACKGROUND = "threshold.dark";
	static final int RED=0, BLACK_AND_WHITE=1, OVER_UNDER=2;
	static final String[] modes = {"Red","B&W", "Over/Under"};
	static final double defaultMinThreshold = 85; 
	static final double defaultMaxThreshold = 170;
	static final int DEFAULT = 0;
    static boolean backgroundToNaN = true;
	static ThresholdAdjuster instance; 
	static int mode = RED;	
	static String[] methodNames = AutoThresholder.getMethods();
	static String method = methodNames[DEFAULT];
	static AutoThresholder thresholder = new AutoThresholder();
	ThresholdPlot plot = new ThresholdPlot();
	Thread thread;
	
	int minValue = -1;
	int maxValue = -1;
	int sliderRange = 256;
	boolean doAutoAdjust,doReset,doApplyLut,doStateChange,doSet;
	
	Panel panel;
	Button autoB, resetB, applyB, setB;
	int previousImageID;
	int previousImageType;
	double previousMin, previousMax;
	int previousSlice;
	ImageJ ij;
	double minThreshold, maxThreshold;  // 0-255
	Scrollbar minSlider, maxSlider;
	Label label1, label2;
	boolean done;
	boolean invertedLut;
	int lutColor;	
	Choice methodChoice, modeChoice;
	Checkbox darkBackground, stackHistogram;
	boolean firstActivation;
	boolean useExistingTheshold;


    public synchronized void adjustmentValueChanged(AdjustmentEvent e) {
		if (e.getSource()==minSlider)
			minValue = minSlider.getValue();
		else
			maxValue = maxSlider.getValue();
		notify();
	}

	public synchronized  void actionPerformed(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b==null) return;
		if (b==resetB)
			doReset = true;
		else if (b==autoB)
			doAutoAdjust = true;
		else if (b==applyB)
			doApplyLut = true;
		else if (b==setB)
			doSet = true;
		notify();
	}
	
	void setLutColor(int mode) {
		switch (mode) {
			case RED:
				lutColor = ImageProcessor.RED_LUT;
				break;
			case BLACK_AND_WHITE:
				lutColor = ImageProcessor.BLACK_AND_WHITE_LUT;
				break;
			case OVER_UNDER:
				lutColor = ImageProcessor.OVER_UNDER_LUT; 
				break;
		}
	}
	
	public synchronized void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		if (source==methodChoice) {
			method = methodChoice.getSelectedItem();
			doAutoAdjust = true;
		} else if (source==modeChoice) {
			mode = modeChoice.getSelectedIndex();
			setLutColor(mode);
			doStateChange = true;
		} else
			doAutoAdjust = true;
		notify();
	}

	ImageProcessor setup(ImagePlus imp) {
		ImageProcessor ip;
		int type = imp.getType();
		if (type==ImagePlus.COLOR_RGB || (imp.isComposite()&&((CompositeImage)imp).getMode()==CompositeImage.COMPOSITE))
			return null;
		ip = imp.getProcessor();
		boolean minMaxChange = false;		
        boolean not8Bits = type==ImagePlus.GRAY16 || type==ImagePlus.GRAY32;
		int slice = imp.getCurrentSlice();
		if (not8Bits) {
			if (ip.getMin()==plot.stackMin && ip.getMax()==plot.stackMax)
				minMaxChange = false;
			else if (ip.getMin()!=previousMin || ip.getMax()!=previousMax) {
				minMaxChange = true;
	 			previousMin = ip.getMin();
	 			previousMax = ip.getMax();
	 		} else if (slice!=previousSlice)
	 			minMaxChange = true;
		}
		int id = imp.getID();
		if (minMaxChange || id!=previousImageID || type!=previousImageType) {
            //IJ.log(minMaxChange +"  "+ (id!=previousImageID)+"  "+(type!=previousImageType));
            Undo.reset();
            if (not8Bits && minMaxChange && !useExistingTheshold) {
                ip.resetMinAndMax();
                imp.updateAndDraw();
            }
			useExistingTheshold = false;
			invertedLut = imp.isInvertedLut();
			minThreshold = ip.getMinThreshold();
			maxThreshold = ip.getMaxThreshold();
			ImageStatistics stats = plot.setHistogram(imp, entireStack(imp));
			if (minThreshold==ImageProcessor.NO_THRESHOLD)
				autoSetLevels(ip, stats);
			else {
				minThreshold = scaleDown(ip, minThreshold);
				maxThreshold = scaleDown(ip, maxThreshold);
			}
			scaleUpAndSet(ip, minThreshold, maxThreshold);
			updateLabels(imp, ip);
			updatePlot();
			updateScrollBars();
			imp.updateAndDraw();
		}
	 	previousImageID = id;
	 	previousImageType = type;
	 	previousSlice = slice;
	 	return ip;
	}
	
    boolean entireStack(ImagePlus imp) {
        return stackHistogram!=null && stackHistogram.getState() && imp.getStackSize()>1;
    }

	void autoSetLevels(ImageProcessor ip, ImageStatistics stats) {
		if (stats==null || stats.histogram==null) {
			minThreshold = defaultMinThreshold;
			maxThreshold = defaultMaxThreshold;
			return;
		}
		//int threshold = ip.getAutoThreshold(stats.histogram);
		boolean darkb = darkBackground!=null && darkBackground.getState();
		int modifiedModeCount = stats.histogram[stats.mode];
		if (!method.equals(methodNames[DEFAULT]))
			stats.histogram[stats.mode] = plot.originalModeCount;
		int threshold = thresholder.getThreshold(method, stats.histogram);
		stats.histogram[stats.mode] = modifiedModeCount;
		if (darkb) {
			if (invertedLut)
				{minThreshold=0; maxThreshold=threshold;}
			else
				{minThreshold=threshold+1; maxThreshold=255;}
		} else {
			if (invertedLut)
				{minThreshold=threshold+1; maxThreshold=255;}
			else
				{minThreshold=0; maxThreshold=threshold;}
		}
		if (minThreshold>255) minThreshold = 255;
		if (Recorder.record) {
			boolean stack = stackHistogram!=null && stackHistogram.getState();
			String options = method+(darkb?" dark":"")+(stack?" stack":"");
			if (Recorder.scriptMode())
				Recorder.recordCall("IJ.setAutoThreshold(imp, \""+options+"\");");
			else
				Recorder.record("setAutoThreshold", options);
		}
	}
	
	/** Scales threshold levels in the range 0-255 to the actual levels. */
	void scaleUpAndSet(ImageProcessor ip, double minThreshold, double maxThreshold) {
		if (!(ip instanceof ByteProcessor) && minThreshold!=ImageProcessor.NO_THRESHOLD) {
			double min = ip.getMin();
			double max = ip.getMax();
			if (max>min) {
				minThreshold = min + (minThreshold/255.0)*(max-min);
				maxThreshold = min + (maxThreshold/255.0)*(max-min);
			} else
				minThreshold = maxThreshold = min;
		}
		ip.setThreshold(minThreshold, maxThreshold, lutColor);
		ip.setSnapshotPixels(null); // disable undo
	}

	/** Scales a threshold level to the range 0-255. */
	double scaleDown(ImageProcessor ip, double threshold) {
		if (ip instanceof ByteProcessor)
			return threshold;
		double min = ip.getMin();
		double max = ip.getMax();
		if (max>min)
			return ((threshold-min)/(max-min))*255.0;
		else
			return ImageProcessor.NO_THRESHOLD;
	}
	
	/** Scales a threshold level in the range 0-255 to the actual level. */
	double scaleUp(ImageProcessor ip, double threshold) {
		double min = ip.getMin();
		double max = ip.getMax();
		if (max>min)
			return min + (threshold/255.0)*(max-min);
		else
			return ImageProcessor.NO_THRESHOLD;
	}

	void updatePlot() {
		plot.minThreshold = minThreshold;
		plot.maxThreshold = maxThreshold;
		plot.mode = mode;
		plot.repaint();
	}
	
	void updateLabels(ImagePlus imp, ImageProcessor ip) {
		double min = ip.getMinThreshold();
		double max = ip.getMaxThreshold();
		if (min==ImageProcessor.NO_THRESHOLD) {
			label1.setText("");
			label2.setText("");
		} else {
			Calibration cal = imp.getCalibration();
			if (cal.calibrated()) {
				min = cal.getCValue((int)min);
				max = cal.getCValue((int)max);
			}
			if (((int)min==min && (int)max==max) || (ip instanceof ShortProcessor)) {
				label1.setText(""+(int)min);
				label2.setText(""+(int)max);
			} else {
				label1.setText(""+IJ.d2s(min,2));
				label2.setText(""+IJ.d2s(max,2));
			}
		}
	}

	void updateScrollBars() {
		minSlider.setValue((int)minThreshold);
		maxSlider.setValue((int)maxThreshold);
	}

    void adjustMinThreshold(ImagePlus imp, ImageProcessor ip, double value) {
		if (IJ.altKeyDown() || IJ.shiftKeyDown() ) {
			double width = maxThreshold-minThreshold;
			if (width<1.0) width = 1.0;
			minThreshold = value;
			maxThreshold = minThreshold+width;
			if ((minThreshold+width)>255) {
				minThreshold = 255-width;
				maxThreshold = minThreshold+width;
				minSlider.setValue((int)minThreshold);
			}
			maxSlider.setValue((int)maxThreshold);
			scaleUpAndSet(ip, minThreshold, maxThreshold);
			return;
		}
		minThreshold = value;
		if (maxThreshold<minThreshold) {
			maxThreshold = minThreshold;
			maxSlider.setValue((int)maxThreshold);
		}
		scaleUpAndSet(ip, minThreshold, maxThreshold);
	}

	void adjustMaxThreshold(ImagePlus imp, ImageProcessor ip, int cvalue) {
		maxThreshold = cvalue;
		if (minThreshold>maxThreshold) {
			minThreshold = maxThreshold;
			minSlider.setValue((int)minThreshold);
		}
		scaleUpAndSet(ip, minThreshold, maxThreshold);
		IJ.setKeyUp(KeyEvent.VK_ALT);
		IJ.setKeyUp(KeyEvent.VK_SHIFT);
	}

	void reset(ImagePlus imp, ImageProcessor ip) {
		ip.resetThreshold();
		ImageStatistics stats = plot.setHistogram(imp, entireStack(imp));
		if (!(ip instanceof ByteProcessor)) {
			if (entireStack(imp))
				ip.setMinAndMax(stats.min, stats.max);
			else
				ip.resetMinAndMax();
		}
		updateScrollBars();
		if (Recorder.record) {
			if (Recorder.scriptMode())
				Recorder.recordCall("IJ.resetThreshold(imp);");
			else
				Recorder.record("resetThreshold");
		}
	}

	void doSet(ImagePlus imp, ImageProcessor ip) {
		double level1 = ip.getMinThreshold();
		double level2 = ip.getMaxThreshold();
		if (level1==ImageProcessor.NO_THRESHOLD) {
			level1 = scaleUp(ip, defaultMinThreshold);
			level2 = scaleUp(ip, defaultMaxThreshold);
		}
		Calibration cal = imp.getCalibration();
		int digits = (ip instanceof FloatProcessor)||cal.calibrated()?2:0;
		level1 = cal.getCValue(level1);
		level2 = cal.getCValue(level2);
		GenericDialog gd = new GenericDialog("Set Threshold Levels");
		gd.addNumericField("Lower Threshold Level: ", level1, digits);
		gd.addNumericField("Upper Threshold Level: ", level2, digits);
		gd.showDialog();
		if (gd.wasCanceled())
			return;
		level1 = gd.getNextNumber();
		level2 = gd.getNextNumber();
		level1 = cal.getRawValue(level1);
		level2 = cal.getRawValue(level2);
		if (level2<level1)
			level2 = level1;
		double minDisplay = ip.getMin();
		double maxDisplay = ip.getMax();
		ip.resetMinAndMax();
		double minValue = ip.getMin();
		double maxValue = ip.getMax();
		if (level1<minValue) level1 = minValue;
		if (level2>maxValue) level2 = maxValue;
		IJ.wait(500);
		ip.setThreshold(level1, level2, lutColor);
		ip.setSnapshotPixels(null); // disable undo
		setup(imp);
		if (Recorder.record) {
			if (imp.getBitDepth()==32) {
				if (Recorder.scriptMode())
					Recorder.recordCall("IJ.setThreshold("+ip.getMinThreshold()+", "+ip.getMaxThreshold()+");");
				else
					Recorder.record("setThreshold", ip.getMinThreshold(), ip.getMaxThreshold());
			} else {
				int min = (int)ip.getMinThreshold();
				int max = (int)ip.getMaxThreshold();
				if (cal.isSigned16Bit()) {
					min = (int)cal.getCValue(level1);
					max = (int)cal.getCValue(level2);
				}
				if (Recorder.scriptMode())
					Recorder.recordCall("IJ.setThreshold(imp, "+min+", "+max+");");
				else
					Recorder.record("setThreshold", min, max);
			}
		}
	}

	void changeState(ImagePlus imp, ImageProcessor ip) {
		scaleUpAndSet(ip, minThreshold, maxThreshold);
		updateScrollBars();
	}

	void autoThreshold(ImagePlus imp, ImageProcessor ip) {
		ip.resetThreshold();
		previousImageID = 0;
		setup(imp);
 	}
 	
 	void apply(ImagePlus imp) {
 		try {
 			if (imp.getBitDepth()==32) {
				GenericDialog gd = new GenericDialog("NaN Backround");
				gd.addCheckbox("Set Background Pixels to NaN", backgroundToNaN);
				gd.showDialog();
				if (gd.wasCanceled()) {
 					runThresholdCommand();
					return;
				}
				backgroundToNaN = gd.getNextBoolean();
				if (backgroundToNaN) {
					Recorder.recordInMacros = true;
 					IJ.run("NaN Background");
					Recorder.recordInMacros = false;
 				} else
 					runThresholdCommand();
 			} else
 				runThresholdCommand();
 		} catch (Exception e)
 			{/* do nothing */}
 		//close();
 	}
 	
 	void runThresholdCommand() {
		Recorder.recordInMacros = true;
 		IJ.run("Convert to Mask");
		Recorder.recordInMacros = false;
 	}
	
	static final int RESET=0;
    static final int AUTO=1;
    static final int APPLY=3;
    static final int STATE_CHANGE=4;
    static final int MIN_THRESHOLD=5;
    static final int MAX_THRESHOLD=6;
    static final int SET=7;

	// Separate thread that does the potentially time-consuming processing 
	public void run() {
		while (!done) {
			synchronized(this) {
				try {wait();}
				catch(InterruptedException e) {}
			}
			doUpdate();
		}
	}

	void doUpdate() {
		ImagePlus imp;
		ImageProcessor ip;
		int action;
		int min = minValue;
		int max = maxValue;
		if (doReset) action = RESET;
		else if (doAutoAdjust) action = AUTO;
		else if (doApplyLut) action = APPLY;
		else if (doStateChange) action = STATE_CHANGE;
		else if (doSet) action = SET;
		else if (minValue>=0) action = MIN_THRESHOLD;
		else if (maxValue>=0) action = MAX_THRESHOLD;
		else return;
		minValue = -1;
		maxValue = -1;
		doReset = false;
		doAutoAdjust = false;
		doApplyLut = false;
		doStateChange = false;
		doSet = false;
		imp = WindowManager.getCurrentImage();
		if (imp==null) {
			IJ.beep();
			IJ.showStatus("No image");
			return;
		}
		ip = setup(imp);
		if (ip==null) {
			imp.unlock();
			IJ.beep();
			if (imp.isComposite())
				IJ.showStatus("\"Composite\" mode images cannot be thresholded");
			else
				IJ.showStatus("RGB images cannot be thresholded");
			return;
		}
		//IJ.write("setup: "+(imp==null?"null":imp.getTitle()));
		switch (action) {
			case RESET: reset(imp, ip); break;
			case AUTO: autoThreshold(imp, ip); break;
			case APPLY: apply(imp); break;
			case STATE_CHANGE: changeState(imp, ip); break;
			case SET: doSet(imp, ip); break;
			case MIN_THRESHOLD: adjustMinThreshold(imp, ip, min); break;
			case MAX_THRESHOLD: adjustMaxThreshold(imp, ip, max); break;
		}
		updatePlot();
		updateLabels(imp, ip);
		ip.setLutAnimation(true);
		imp.updateAndDraw();
	}

    /** Overrides close() in PlugInFrame. */
    public void close() {
    	super.close();
		instance = null;
		done = true;
		Prefs.saveLocation(LOC_KEY, getLocation());
		Prefs.set(MODE_KEY, mode);
		Prefs.set(DARK_BACKGROUND, darkBackground.getState());
		synchronized(this) {
			notify();
		}
	}

    public void windowActivated(WindowEvent e) {
    	super.windowActivated(e);
    	plot.requestFocus();
		ImagePlus imp = WindowManager.getCurrentImage();
		if (imp!=null && firstActivation) {
			previousImageID = 0;
			useExistingTheshold = isThresholded(imp);
			setup(imp);
			firstActivation = false;
		}
	}
	
	boolean isThresholded(ImagePlus imp) {
		ImageProcessor ip = imp.getProcessor();
		return ip.getMinThreshold()!=ImageProcessor.NO_THRESHOLD && ip.isColorLut();
	}

    /** Notifies the ThresholdAdjuster that the image has changed. */
    public static void update() {
		if (instance!=null) {
			ThresholdAdjuster ta = instance;
			ImagePlus imp = WindowManager.getCurrentImage();
			if (imp!=null && ta.previousImageID==imp.getID()) {
				ta.previousImageID = 0;
				ta.setup(imp);
			}
		}
    }

	/** Returns the current method ("Default", "Huang", etc). */
	public static String getMethod() {
		return method;
	}
	
	/** Returns the current mode ("Red","B&W" or"Over/Under"). */
	public static String getMode() {
		return modes[mode];
	}

} // ThresholdAdjuster class


class ThresholdPlot extends Canvas implements Measurements, MouseListener {
	static final int WIDTH = 256, HEIGHT=48;
	double minThreshold = 85;
	double maxThreshold = 170;
	ImageStatistics stats;
	int[] histogram;
	Color[] hColors;
	int hmax;
	Image os;
	Graphics osg;
	int mode;
	int originalModeCount;
	double stackMin, stackMax;
	int imageID2;
	boolean entireStack2;
	double mean2;
	
	public ThresholdPlot() {
		addMouseListener(this);
		setSize(WIDTH+1, HEIGHT+1);
	}
	
    /** Overrides Component getPreferredSize(). Added to work 
    	around a bug in Java 1.4.1 on Mac OS X.*/
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH+1, HEIGHT+1);
    }
    
	ImageStatistics setHistogram(ImagePlus imp, boolean entireStack) {
		double mean = entireStack?imp.getProcessor().getStatistics().mean:0.0;
		if (entireStack && stats!=null && imp.getID()==imageID2 
		&& entireStack==entireStack2 && mean==mean2)
			return stats;
		mean2 = mean;
		ImageProcessor ip = imp.getProcessor();
		stats = null;
		if (entireStack) {
			if (imp.isHyperStack()) {
				ImageStack stack = ChannelSplitter.getChannel(imp, imp.getChannel());
				stats = new StackStatistics(new ImagePlus("", stack));
			} else
				stats = new StackStatistics(imp);
		}
		if (!(ip instanceof ByteProcessor)) {
			if (entireStack) {
				if (imp.getLocalCalibration().isSigned16Bit()) 
					{stats.min += 32768; stats.max += 32768;}
				stackMin = stats.min;
				stackMax = stats.max;
				ip.setMinAndMax(stackMin, stackMax);
				imp.updateAndDraw();
			} else {
				stackMin = stackMax = 0.0;
				if (entireStack2) {
					ip.resetMinAndMax();
					imp.updateAndDraw();
				}
			}
			Calibration cal = imp.getCalibration();
			if (ip instanceof FloatProcessor) {
				int digits = Math.max(Analyzer.getPrecision(), 2);
				IJ.showStatus("min="+IJ.d2s(ip.getMin(),digits)+", max="+IJ.d2s(ip.getMax(),digits));
			} else
				IJ.showStatus("min="+(int)cal.getCValue(ip.getMin())+", max="+(int)cal.getCValue(ip.getMax()));
			ip = ip.convertToByte(true);
			ip.setColorModel(ip.getDefaultColorModel());
		}
		Roi roi = imp.getRoi();
		if (roi!=null && !roi.isArea()) roi = null;
		ip.setRoi(roi);
		if (stats==null)
			stats = ImageStatistics.getStatistics(ip, AREA+MIN_MAX+MODE, null);
		int maxCount2 = 0;
		histogram = stats.histogram;
		originalModeCount = histogram[stats.mode];
		for (int i = 0; i < stats.nBins; i++)
			if ((histogram[i] > maxCount2) && (i != stats.mode))
				maxCount2 = histogram[i];
		hmax = stats.maxCount;
		if ((hmax>(maxCount2 * 2)) && (maxCount2 != 0)) {
			hmax = (int)(maxCount2 * 1.5);
			histogram[stats.mode] = hmax;
        	}
		os = null;

   		ColorModel cm = ip.getColorModel();
		if (!(cm instanceof IndexColorModel))
			return null;
		IndexColorModel icm = (IndexColorModel)cm;
		int mapSize = icm.getMapSize();
		if (mapSize!=256)
			return null;
		byte[] r = new byte[256];
		byte[] g = new byte[256];
		byte[] b = new byte[256];
		icm.getReds(r); 
		icm.getGreens(g); 
		icm.getBlues(b);
		hColors = new Color[256];
		for (int i=0; i<256; i++)
			hColors[i] = new Color(r[i]&255, g[i]&255, b[i]&255);
		imageID2 = imp.getID();
		entireStack2 = entireStack;
		return stats;
	}
	
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		if (g==null) return;
		if (histogram!=null) {
			if (os==null && hmax>0) {
				os = createImage(WIDTH,HEIGHT);
				osg = os.getGraphics();
				osg.setColor(Color.white);
				osg.fillRect(0, 0, WIDTH, HEIGHT);
				osg.setColor(Color.gray);
				for (int i = 0; i < WIDTH; i++) {
					if (hColors!=null) osg.setColor(hColors[i]);
					osg.drawLine(i, HEIGHT, i, HEIGHT - (HEIGHT * histogram[i] /hmax));
				}
				osg.dispose();
			}
			if (os==null) return;
			g.drawImage(os, 0, 0, this);
		} else {
			g.setColor(Color.white);
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		g.setColor(Color.black);
 		g.drawRect(0, 0, WIDTH, HEIGHT);
 		if (mode==ThresholdAdjuster.RED)
			g.setColor(Color.red);
		else if (mode==ThresholdAdjuster.OVER_UNDER) {
			g.setColor(Color.blue);
 			g.drawRect(1, 1, (int)minThreshold-2, HEIGHT);
 			g.drawRect(1, 0, (int)minThreshold-2, 0);
 			g.setColor(Color.green);
 			g.drawRect((int)maxThreshold+1, 1, WIDTH-(int)maxThreshold, HEIGHT);
 			g.drawRect((int)maxThreshold+1, 0, WIDTH-(int)maxThreshold, 0);
			return;
		}
 		g.drawRect((int)minThreshold, 1, (int)(maxThreshold-minThreshold), HEIGHT);
 		g.drawLine((int)minThreshold, 0, (int)maxThreshold, 0);
     }

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}

} // ThresholdPlot class
