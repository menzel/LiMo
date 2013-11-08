package de.thm.bi.recognition.normalize;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.Thresholder;
import ij.process.ImageConverter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.Point;
import de.thm.bi.recognition.data.dataset.DataSetFactory;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.utils.Preferences;
import de.thm.bi.utils.StopWatch;
import de.thm.bi.utils.Vector3D;
import de.thm.bi.utils.Vector3DPool;

/**
 * This Analyzer uses ImageJ with its Colour_Deconvolution Plugin to split and
 * analyze he pc points.
 * 
 * @author Artur Klos
 * 
 */
public class ImageJ implements INormalization {

	@Override
	public IDataSet normalize(ImagePlus img) {
		StopWatch.start();
		ArrayList<IPoint> points = new ArrayList<IPoint>(100000);
//		img.show();
		// Calculate grayscale
		ImagePlus grayscale = img.duplicate();
		new ImageConverter(grayscale).convertToGray8();
//		grayscale.show();

		// Calculate Treshhold to determine weather this point is a nuclei point
		// or not
		ImagePlus treshHold = img.duplicate();
		IJ.runPlugIn(treshHold, Thresholder.class.getName(), "");
//		treshHold.show();

		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage nucleiImg = img.getBufferedImage();

		int min = Integer.MAX_VALUE, max = 0;

		// Attach all points with their attributes to the dataset that are
		// filtered by the threshold
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				int[] pixel = treshHold.getPixel(i, j);
				if (pixel[0] == 255) {
					Color color = new Color(nucleiImg.getRGB(i, j));

					Vector3D tmpVec = Vector3DPool.obtain(color.getRed(), color.getGreen(), color.getBlue());

					IPoint point = new Point(Classification.UNCLASSIFIED, i, j, color);
					point.setColorVector(tmpVec);

					int gs = grayscale.getPixel(i, j)[0];
					point.storeAttribute(Attributes.GRAYSCALE_INT, gs);
					min = Math.min(min, gs);
					max = Math.max(max, gs);

					points.add(point);
				}
			}
		}

		IDataSet dataSet = DataSetFactory.createDefault(points);

		dataSet.storeAttribute(Attributes.MIN_GRAYSCALE_INT, min);
		dataSet.storeAttribute(Attributes.MIN_GRAYSCALE_INT, max);
		dataSet.storeAttribute(Attributes.IMAGE_WIDTH, img.getWidth());
		dataSet.storeAttribute(Attributes.IMAGE_HEIGHT, img.getHeight());

		// Close windows if enabeled
		if (Preferences.isDontShowImageJWindowsEnabled()) {
			WindowManager.closeAllWindows();
		}

		StopWatch.stopAndLog("ImageJ Analyzation");
		return dataSet;
	}
}
