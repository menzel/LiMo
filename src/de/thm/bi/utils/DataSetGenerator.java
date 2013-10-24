package de.thm.bi.utils;

import java.util.ArrayList;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.Point;
import de.thm.bi.recognition.data.dataset.DataSetFactory;
import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * Class for generating random DataSets / Points
 * 
 * @author Artur Klos
 * 
 */
public class DataSetGenerator {

	private static final IPoint createRandomPoint(int xmin, int xmax, int ymin, int ymax) {
		return new Point(MathUtils.random(xmin, xmax), MathUtils.random(ymin, ymax));
	}

	public static final IDataSet generateRandomDataSet() {

		final int xmin = 0, xmax = 500, ymin = 0, ymax = 500;

		ArrayList<IPoint> points = new ArrayList<>();

		int numberOfPoints = 2000;

		for (int i = 0; i < numberOfPoints; i++) {
			points.add(createRandomPoint(xmin, xmax, ymin, ymax));
		}

		return DataSetFactory.createDefault(points);
	}
}
