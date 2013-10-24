package de.thm.bi.recognition.filter;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.utils.Vector3D;

/**
 * Filter that filters points by the average colorvector of a DataSet Points
 * with a intensity above the average are also filtered
 * 
 * @author Artur Klos
 */
public class ColorAngle implements IPointFilter {

	/**
	 * An Angle of 1.5 degrees seems to be a good choice
	 */
	private static final float MAX_ANGLE = 1.5f;

	private Vector3D averageColor = new Vector3D();
	private float averageLength = 0;

	/**
	 * Extract the averageColor and the average length of the colorvector
	 * 
	 * @param dataSet
	 *            DataSet that will be analyzed
	 */
	private void analyzeDataSet(IDataSet dataSet) {
		List<IPoint> points = dataSet.getAllPoints();

		int limit = points.size();

		float r = 0;
		float g = 0;
		float b = 0;
		averageLength = 0;

		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			Vector3D vec = point.getColorVector();
			averageLength += vec.magnitude();
			r += vec.x;
			g += vec.y;
			b += vec.z;
		}

		r /= limit;
		g /= limit;
		b /= limit;
		averageLength /= limit;

		averageColor.x = r;
		averageColor.y = g;
		averageColor.z = b;

	}

	/**
	 * @param point
	 *            Checks weather the point has to be filtered or not
	 * @return Returns false if the point has to be filtered true if not
	 */
	private boolean checkCriteria(IPoint point) {
		Vector3D v = point.getColorVector();
		float length = v.magnitude();
		if (averageLength + 10 < length) {
			double angle = Vector3D.org(averageColor, point.getColorVector()) * 180.0 / Math.PI;
			return angle < MAX_ANGLE;
		}
		return true;

	}

	@Override
	public List<IPoint> filter(IDataSet dataSet) {
		analyzeDataSet(dataSet);
		ArrayList<IPoint> filteredPoints = new ArrayList<>();

		List<IPoint> allPoints = dataSet.getAllPoints();
		int limit = allPoints.size();

		for (int i = 0; i < limit; i++) {
			IPoint point = allPoints.get(i);
			if (!checkCriteria(point)) {
				filteredPoints.add(point);
			}
		}

		return filteredPoints;
	}
}
