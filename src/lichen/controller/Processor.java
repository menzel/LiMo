package lichen.controller;

import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.Colour_Deconvolution;
import ij.plugin.filter.BackgroundSubtracter;
import ij.plugin.filter.GaussianBlur;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import lichen.view.MainGUI;


/**
 * Represents an Processor Object which processes Images 
 * @author menzel
 *
 */
public class Processor {

	private double particleMinSize = 10.0;
	private double particleMaxSize = Double.MAX_VALUE;
	private ResultsTable rt;
	private static double sheetWidth = 297;

	public Processor() {
	}

	/**
	 * Analyzes any ImagePlus picture
	 * @pre imp must be binary
	 * @param imp - binary picture (use Processor.getBinary() )
	 * @return double area of this picture 
	 * <p>To prevent this method from printing Results or showing the picture modify 
	 * the new ParticleAnalyzer(....</p>
	 */
	public Double analyzeArea(ImagePlus imp){ 


		ParticleAnalyzer w = new  ParticleAnalyzer(ParticleAnalyzer.SHOW_SUMMARY+ParticleAnalyzer.SHOW_OVERLAY_MASKS,
				Measurements.AREA, rt, particleMinSize, particleMaxSize, 0,1); 

		//			ParticleAnalyzer w = new  ParticleAnalyzer(ParticleAnalyzer.SHOW_OUTLINES,
		//					Measurements.AREA, rt, particleMinSize, particleMaxSize, 0,1); 


	//	Calibration cali = imp.getCalibration(); 
	//	cali.setUnit("mm"); 

		//	cali.pixelWidth = getPixelRate(imp);
		//	cali.pixelHeight = getPixelRate(imp); 
		//		MeasurementsFactory.getInstance().setPixelrate(getPixelRate(imp));
 
		//	rt.show("Flechten Fläche");

		//	imp.setProcessor(new ByteProcessor(imp.getProcessor(), true));
		//	((ByteProcessor) imp.getProcessor()).skeletonize();


		Double d =  (Double) w.analyze(imp); 
		if(d == null){
			System.out.println("Area analyzation failed..");
			return null;
		}else{
			return d;
		} 
	}

	//	show.showPicture(imp);


	/**
	 * Accepts a double and converts the number according to the calibration from pixels to square mm
	 * @param d - pixel width
	 * @param imp - image, if null the image from MainGui is taken
	 * @return 
	 */
	public double convertFromPixel(double d, ImagePlus imp) { 
		if(imp == null)
			imp = MainGUI.getInstance().getImp();
		return d/getPixelRate(imp);
	}

	/**
	 * calculates the (Pixel/mm) ^2 rate of an DIN A4 Paper Sheet
	 * @pre imp should be a DIN A4 Paper Sheet
	 * @param imp- image to be calculated 
	 * @return - Pixel per mm squared on imp
	 */
	public double getPixelRate(ImagePlus imp) {

		if(imp == null)
			imp = MainGUI.getInstance().getImp(); 

		double pixelN = (imp.getWidth() > imp.getHeight())? imp.getWidth(): imp.getHeight(); 
		return (Math.pow(pixelN/sheetWidth,2)); 
	}

	/**
	 * creates a Particle- analyzable ImagePlus picture
	 * @post picture is Gray8 and on autoThreshold
	 * @param imp - to be converted
	 * @return - analyzable ImagePlus picture
	 */
	public ImagePlus getBinary(ImagePlus imp) {

		ImageProcessor ip = imp.getProcessor();
		ip = ip.convertToByte(true);
		ip.autoThreshold();

		return new ImagePlus("", ip);

	}

	/**
	 * creates a Particle- analyzable ImagePlus picture
	 * @post picture is Gray8 and threshold  
	 * @param imp - to be converted
	 * @param threshold - threshold applied to the picture
	 * @return - analyzable ImagePlus picture
	 */
	public ImagePlus getBinary(ImagePlus imp, int threshold) {

		ImageConverter con = new ImageConverter(imp);
		con.convertToGray8();

		ByteProcessor b = new ByteProcessor(imp.getImage());
		b.threshold(threshold);

		return new ImagePlus("bit", b); 
	}

	/**
	 * Splits an ImagePlus picture by colors 
	 * @param imp - image to be split
	 * @param colorCount - count of colors on the image
	 * @return ImageStack of Pictures with one color on each slice
	 */
	public ImageStack splitColors(ImagePlus imp, int colorCount){ 

		Colour_Deconvolution dec = new Colour_Deconvolution(); 
		ImageConverter con = new ImageConverter(imp);
		dec.setUp("H&E 2", false, false);

		BackgroundSubtracter sub  = new BackgroundSubtracter();
		sub.setup("", imp); 

		con.convertToRGB(); 
		//		con.convertRGBtoIndexedColor(colorCount); // posterization
		ImageStack stack = dec.deconvolve("wtf", imp);

		return stack;
	}

	/**
	 * @return the particleMinSize
	 */
	public double getParticleMinSize() {
		return particleMinSize;
	}

	/**
	 * @return the particleMaxSize
	 */
	public double getParticleMaxSize() {
		return particleMaxSize;
	}

	/**
	 * @param particleMinSize the particleMinSize to set
	 */
	public void setParticleMinSize(double particleMinSize) {
		this.particleMinSize = particleMinSize;
	}

	/**
	 * @param particleMaxSize the particleMaxSize to set
	 */
	public void setParticleMaxSize(double particleMaxSize) {
		this.particleMaxSize = particleMaxSize;
	}

	/**
	 * Substracts the background from images
	 * @param imp- image
	 * @param radius- ball radius 
	 * @deprecated
	 */
	public ImagePlus substrackBackground(ImagePlus imp, int radius) {

		BackgroundSubtracter sub = new BackgroundSubtracter();
		sub.rollingBallBackground(imp.getProcessor(), radius, false, true, false, true, true);
		imp.updateAndDraw();

		return imp;

	}

	/**
	 * Analyze a single particle
	 * @param i - position value 
	 * @param j - position value
	 * @param imp - image to analyze
	 * 
	 */
	public void selectParticle(int i, int j, ImagePlus imp) {
		ParticleAnalyzer w = new  ParticleAnalyzer(ParticleAnalyzer.SHOW_SUMMARY+ParticleAnalyzer.SHOW_OUTLINES,
				Measurements.AREA, rt, particleMinSize, particleMaxSize, 0,1); 

		w.analyzeParticle(i, j, imp,  imp.getProcessor());

	}

	/**
	 * Posterizes an image
	 * @param imp image to be proccessed
	 * @param cc - color count
	 */
	public void posterize(ImagePlus imp, int cc) {
		System.out.println("here");
		int[] pixels = (int[]) imp.getProcessor().getPixels();
		int r,g,b;

		for(int i =0; i< pixels.length; i++){
			int c = pixels[i];

			r = (c&0xff0000)>>16;	
		g =(c&0xff00)>>8;
		b = c&0xff;


//		r  = posterize(r,cc);
//		g = posterize(g,cc);
//		b = posterize(b,cc);

		if(g > 210){
			g = 255;

		}
		if(b > 210){
			b = 255;
		} 

		if(r > 210){
			r = 255;
		}

		pixels[i] =  ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff); 

		}



	}


	/**
	 * enhance background from image with gaussian blur
	 * @param imp - image 
	 * @param bl -  blur value 
	 */
	public void background(ImagePlus imp, double bl) {
		ImageProcessor processor = imp.getProcessor();

		//		ContrastEnhancer contrast = new ContrastEnhancer();
		//		contrast.stretchHistogram(imp, 3);

		GaussianBlur blur = new GaussianBlur(); 
		
		blur.blurGaussian(processor, bl,bl, 0.9); 


		imp.getProcessor().sharpen();
		int[] pixels;
		//TODO: rewrite with instanceof

		try{

			pixels = (int[]) processor.getPixels();

		}catch (ClassCastException e){

			byte[] barr = (byte[]) processor.getPixels();
			pixels = new int[barr.length];

			for(int i = 0; i < barr.length; i++){
				if(barr[i] == 0){
					pixels[i]  = 0;
				}else{
//					if(barr[i]  == 0){ 
						pixels[i]  = -1;
//					}
				}
			} 
		}

		int[] pixels2 = new int[pixels.length]; 
		int r,g,b,count = 0;

		for(int c :pixels ){
			r = (c&0xff0000)>>16;	
			g =(c&0xff00)>>8;
		b = c&0xff;

		if(r > 210 && g > 210 && b > 210){
			pixels2[count] = -1;

		}else{ 
			pixels2[count] =  (((int)r&0x0ff)<<16)|(((int)g&0x0ff)<<8)|((int)b&0x0ff); 
		} 
		count++;
		}

		imp.setProcessor("new", new ColorProcessor(imp.getWidth(), imp.getHeight(),pixels2));

	}

	/**
	 * @param sheetWidth the sheetWidth to set
	 */
	public static void setSheetWidth(double sheetWidth) {
		Processor.sheetWidth = sheetWidth;
	}
}
