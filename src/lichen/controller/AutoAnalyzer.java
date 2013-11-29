package lichen.controller;

import java.awt.Color;
import java.util.List; 
import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.normalize.NormalizationFactory;
import de.thm.bi.recognition.normalize.NormalizationFactory.ImageAnalyzerStrategies;
import de.thm.bi.recognition.segmentation.algorithm.ColorSegmentation;
import de.thm.bi.utils.Preferences;
import de.thm.bi.utils.Vector3D;
import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
import lichen.view.MainGUI;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ColorProcessor;

/**
 * Auto analyzes a scan of lichen as far as possible
 * @author menzel
 *
 *{@See old version for de.* packages}
 */
public class AutoAnalyzer {

	public AutoAnalyzer() {

	}

	/**
	 * Auto Analyzes an Picture
	 * @post Results are displayed on GUI
	 * @param imp - Image to analyze
	 */
	public int[] autoAnalyze(ImagePlus imp) {
		Processor processor = new Processor(); 
		MainGUI gui = MainGUI.getInstance();
		MeasurementsFactory factory = MeasurementsFactory.getInstance(); 
		
		double rate  = gui.getMyProcessor().getPixelRate(imp);
		factory.setPixelrate(rate); 


		System.out.println("pixelrate: "  + rate);
		Preferences.init();

		Roi r = new Roi(imp.getWidth()/70, imp.getHeight()/85,imp.getWidth() - imp.getWidth()*0.32, imp.getHeight() - imp.getHeight()*0.045); 
		imp.setRoi(r);
		imp.setProcessor(imp.getProcessor().crop());

		//		processor.background(imp, 1);
		IDataSet dataSet = NormalizationFactory.create(ImageAnalyzerStrategies.IMAGEJ).normalize( imp.duplicate()) ; 
		ColorSegmentation colorSegmentation = new ColorSegmentation();
		colorSegmentation.performSegmentation(dataSet);


		int[] imageArray = new int[imp.getWidth()* imp.getHeight()];

		// fill new image with white
		for(int j = 0; j < imageArray.length; j++){
			imageArray[j] = -1; 
		}

		List<IRegion> regions = dataSet.getClusters(); 
		int limit = regions.size();

	

		for (int i = 0; i < limit; i++) {
			System.out.println("processing " + i + ".");

			int[] colorImageArray = new int[imp.getWidth()* imp.getHeight()];

			//			int color = gui.makeColor((int)(Math.random()*170+30), (int)(Math.random()*170+30), (int)(Math.random()*170+30)); 
			Color c = ColorStack.pop();

			int color =((c.getRed()&0x0ff)<<16)|((c.getGreen()&0x0ff)<<8)|(c.getBlue()&0x0ff); 
			IRegion region = regions.get(i); 
			List<IPoint> points = region.getAllPoints();


			//Build Image from seperated points 

			//get middle color:
			Vector3D v = colorSegmentation.getMiddleRegionColor(region);
			Color middleColor =  new Color((int)v.x, (int) v.y, (int) v.z); 

			for(int j = 0; j < colorImageArray.length; j++){
				colorImageArray[j] = -1; 
			}

			for(IPoint point: points){ 
				imageArray[point.getX() + point.getY()*imp.getWidth()]  = color; 
				colorImageArray[point.getX() + point.getY()*imp.getWidth()] = 0;
			}


			imp = new ImagePlus("" , new ColorProcessor(imp.getWidth(), imp.getHeight(), colorImageArray)); 
			imp = processor.getBinary(imp); 
			imp.setTitle( "Nr. " + i+ " Mittlere Farbe: " + middleColor.toString() + " AutoColor: " + c.toString() + " :");

			double area = processor.analyzeArea(imp); 
			System.out.println("area in px:  " + area);
			System.out.println("in mm^2: " + area/rate);
			//	gui.setImp(imp);

			//disable on single windows mode:
			//imp.show();

			Measurement measurement = factory.createMeasurement();
			measurement.setArea(area);
			measurement.setColor(middleColor);
			System.out.println("");
		}


		return imageArray;
	}

}
