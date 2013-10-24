package de.thm.bi.recognition.filter;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.utils.MathUtils;
import de.thm.bi.utils.PointsMatrixUtil;

/**
 * @author Artur Klos Not yet rdy...
 */
public class Curvature implements IPointFilter {

	public enum StoreAttribute {
		CURVATURE, CURVATURE_POINT
	}

	private static final float CURVATURE_LIMIT = 1;

	@Override
	public List<IPoint> filter(IDataSet dataSet) {

		List<IPoint> filteredPoints = new ArrayList<>();

		List<IPoint> hull = dataSet.getFullHull();

		int limit = hull.size();

		float curvatrueAverage = 0;

		for (int i = 0; i < limit; i++) {
			IPoint one = hull.get(i);

			float maxCurvature = Float.MAX_VALUE;

			for (int j = 0; j < limit; j++) {
				IPoint two = hull.get(j);
				if (one != two) {
					if (MathUtils.squaredDistance(one, two) < 10) {
						float calculatedCurvature = MathUtils.calculateCurvature(one, two);
						if (calculatedCurvature > maxCurvature) {
							maxCurvature = calculatedCurvature;
						}
					}
				}
			}
			curvatrueAverage += maxCurvature;
			System.out.println("CURVATURE: " + maxCurvature);
			one.storeAttribute(StoreAttribute.CURVATURE, maxCurvature);
		}
		curvatrueAverage /= limit;

		System.out.println("AVERAGE CURVATURE: " + curvatrueAverage);

		for (int i = 0; i < limit; i++) {
			IPoint one = hull.get(i);

			float minDistance = Float.MAX_VALUE;
			IPoint minDistancePoint = null;

			if ((float) one.getStoredAttribute(StoreAttribute.CURVATURE) > CURVATURE_LIMIT) {

				for (int j = 0; j < limit; j++) {
					IPoint two = hull.get(j);
					if (one != two) {

						if ((float) two.getStoredAttribute(StoreAttribute.CURVATURE) > CURVATURE_LIMIT) {
							float distance = MathUtils.squaredDistance(one, two);
							if (distance < minDistance) {
								minDistance = distance;
								minDistancePoint = two;
							}
						}
					}
				}
				filteredPoints.addAll(PointsMatrixUtil.findPointsOnLine(dataSet, one, minDistancePoint));
			}
		}

		return filteredPoints;
	}
}
