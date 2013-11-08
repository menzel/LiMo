package de.thm.bi.recognition.filter;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.utils.MathUtils;
import de.thm.bi.utils.PointsMatrixUtil;

/**
 * The Lines Between Counter Hull Points Filter filters by getting the
 * "counter hull" point (the point on the other side of the hull) of a hull
 * point and then deleting the line between these two points. Only lines less
 * then a certain variance of the average linesize between these points are
 * filtered
 * 
 * @author Artur Klos
 */
public class LBCHPoints implements IPointFilter {

	public static final String COUNTER_HULL_POINT = "counter_hull_point";
	private static final float MAX_SIZE_OF_AVERAGE = 0.75f;

	@Override
	public List<IPoint> filter(IDataSet dataSet) {

		List<IPoint> filteredPoints = new ArrayList<IPoint>();

		List<IPoint> hull = dataSet.getFullHull();

		int limit = hull.size();

		float sqAvgDistance = 0;
		int numberOfCompareAbleBorderPoints = 0;

		// GETS THE COUNTERBORDER POINT FOR EVERY HULL POINT AND CALCULATING THE
		// AVERAGE DISTANCE BEWTEEN THE LINES
		for (int i = 0; i < limit; i++) {
			IPoint one = hull.get(i);
			IPoint two = PointsMatrixUtil.getCounterHullPoint(dataSet, one);
			if (two != null) {
				one.storeAttribute(COUNTER_HULL_POINT, two);
				numberOfCompareAbleBorderPoints++;
				sqAvgDistance += MathUtils.distance(one, two);
			}
		}
		sqAvgDistance /= numberOfCompareAbleBorderPoints;

		// CHECK WEATHER THE DISTANCE BETWEEN THE COUNTERPOINTS IS LESS THEN THE
		// AVERAGE DISTANCE * MAX_SIZE_OF_AVERATE IF IT MEETS THE CONDITION
		// FILTER THE LINE BETWEEN THESE TWO POINTS
		for (int i = 0; i < limit; i++) {
			IPoint one = hull.get(i);
			IPoint two = (IPoint) one.getStoredAttribute(COUNTER_HULL_POINT);

			if (two != null) {

				float distance = MathUtils.distance(one, two);
				if (distance < sqAvgDistance * MAX_SIZE_OF_AVERAGE) {
					filteredPoints.addAll(PointsMatrixUtil.findPointsOnLine(dataSet, one, two));
				}
			}
		}

		return filteredPoints;
	}
}
