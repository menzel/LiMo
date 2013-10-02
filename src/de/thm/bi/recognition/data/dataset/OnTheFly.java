package de.thm.bi.recognition.data.dataset;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.utils.MathUtils;

/**
 * The OnTheFlyDataSet implements the IDataSet interface. It calculates the
 * distances of points on the fly. It has no further allocations but due to the
 * fact it has to calculate the distances again and again it is very slow.
 * 
 * @author Artur Klos
 * 
 */
public class OnTheFly extends AbstractDataSet {

	OnTheFly(List<IPoint> points) {
		super(points);
	}

	public OnTheFly(List<IPoint> points, PointsMatrix pointsMatrix) {
		super(points, pointsMatrix);
	}

	@Override
	public List<IPoint> getNeighbors(IPoint point, float epsilon) {
		ArrayList<IPoint> result = new ArrayList<IPoint>();

		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			IPoint comaprePoint = points.get(i);
			if (MathUtils.distance(comaprePoint, point) <= epsilon) {
				result.add(comaprePoint);
			}
		}

		return result;
	}
}
