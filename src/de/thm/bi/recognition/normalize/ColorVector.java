package de.thm.bi.recognition.normalize;

import ij.ImagePlus;

import java.awt.Color;
import java.util.ArrayList;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.Point;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.dataset.DataSetFactory;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.utils.Preferences;
import de.thm.bi.utils.StopWatch;
import de.thm.bi.utils.Vector3D;
import de.thm.bi.utils.Vector3DPool;

/**
 * A Strategy that analyzes the Image by using a predefined colorvector to
 * filter all pc points. The predefined colorvector can be found in the
 * Preferences Class {@link Preferences}
 * 
 * @author Artur Klos
 * 
 */
public class ColorVector implements INormalization {

	@Override
	public IDataSet normalize(ImagePlus img) {
		StopWatch.start();
		ArrayList<IPoint> points = new ArrayList<>(100000);

		int w = img.getWidth();
		int h = img.getHeight();

		float maxColorDistance = Preferences.getMaxColorDistance();
		Vector3D comapreVector = Preferences.normalizedNucleiColorVector;

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int[] pixel = img.getPixel(i, j);
				Color color = new Color(pixel[0], pixel[1], pixel[2]);

				Vector3D tmpVec = Vector3DPool.obtain(color.getRed(), color.getGreen(), color.getBlue());
				tmpVec.normalize();
				tmpVec.sub(comapreVector);

				if (Math.abs(tmpVec.x) < maxColorDistance && Math.abs(tmpVec.y) < maxColorDistance
						&& Math.abs(tmpVec.z) < maxColorDistance) {
					IPoint point = new Point(Classification.UNCLASSIFIED, i, j, color);
					point.setColorVector(tmpVec);
					points.add(point);
				} else {
					Vector3DPool.recycle(tmpVec);
				}
			}
		}
		StopWatch.stopAndLog("Colorvector Analyzation");
		return DataSetFactory.createDefault(points);

	}
}
