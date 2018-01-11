package ij.process;

import java.awt.image.*;
import ij.*;
import ij.measure.*;

/** This class converts an ImagePlus object to a different type. */
public class ImageConverter {
	private ImagePlus imp;
	private int type;
	//private static boolean doScaling = Prefs.getBoolean(Prefs.SCALE_CONVERSIONS,true);
	private static boolean doScaling = true;

	/** Constructs an ImageConverter based on an ImagePlus object. */
	public ImageConverter(ImagePlus imp) {
		this.imp = imp;
		type = imp.getType();
	}

	/** Converts this ImagePlus to 8-bit grayscale. */
	public synchronized void convertToGray8() {
		if (imp.getStackSize()>1)
			throw new IllegalArgumentException("Unsupported conversion");
		ImageProcessor ip = imp.getProcessor();
		if (type==ImagePlus.GRAY16 || type==ImagePlus.GRAY32) {
			imp.setProcessor(null, ip.convertToByte(doScaling));
			imp.setCalibration(imp.getCalibration()); //update calibration
		} else if (type==ImagePlus.COLOR_RGB)
	    	imp.setProcessor(null, ip.convertToByte(doScaling));
		else if (ip.isPseudoColorLut()) {
			boolean invertedLut = ip.isInvertedLut();
			ip.setColorModel(LookUpTable.createGrayscaleColorModel(invertedLut));
	    	imp.updateAndDraw();
		} else {
			ip = new ColorProcessor(imp.getImage());
	    	imp.setProcessor(null, ip.convertToByte(doScaling));
	    }
	    ImageProcessor ip2 = imp.getProcessor();
		if (Prefs.useInvertingLut && ip2 instanceof ByteProcessor && !ip2.isInvertedLut()&& !ip2.isColorLut()) {
			ip2.invertLut();
			ip2.invert();
		}
	}

	/** Set true to scale to 0-255 when converting short to byte or float
		to byte and to 0-65535 when converting float to short. */
	public static void setDoScaling(boolean scaleConversions) {
		doScaling = scaleConversions;
		IJ.register(ImageConverter.class); 
	}

	/** Returns true if scaling is enabled. */
	public static boolean getDoScaling() {
		return doScaling;
	}
}
