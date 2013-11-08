package de.thm.bi.recognition.data.dataset;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.utils.MathUtils;

/**
 * The MatrixDataSet implements the IDataSet interface. It uses the points that
 * are provided by all DataSets. Due to the usage of the PointsMatrix the
 * MatrixDataSet has nearly no need for extra allocations and also is a fast
 * implementation.
 * 
 * @author Artur Klos
 * 
 */
public class Matrix extends AbstractDataSet {

	private List<IPoint> clearedPoints;

	Matrix(List<IPoint> points) {
		super(points);
	}

	public Matrix(List<IPoint> points, PointsMatrix pointsMatrix) {
		super(points, pointsMatrix);
	}

	@Override
	public void dataSetChanged() {
		invalidatePointsMatrix();
	}

	private List<IPoint> getEpsilonRangedPoints(IPoint point, float epsilon) {
		ArrayList<IPoint> epsilonRangedList = new ArrayList<IPoint>((int) epsilon * 10);

		int searchRange = (int) (epsilon + 2);

		int iX = point.getX();
		int iY = point.getY();

		int startX = Math.max(pointsMatrix.getMinX(), iX - searchRange);
		int endX = Math.min(pointsMatrix.getMaxX(), iX + searchRange);

		int startY = Math.max(pointsMatrix.getMinY(), iY - searchRange);
		int endY = Math.min(pointsMatrix.getMaxY(), iY + searchRange);

		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				IPoint matrixPoint = pointsMatrix.getPoint(x, y);
				if (matrixPoint != null) {
					epsilonRangedList.add(matrixPoint);
				}
			}
		}
		return epsilonRangedList;
	}

	@Override
	public List<IPoint> getNeighbors(IPoint point, float epsilon) {
		ArrayList<IPoint> neighbors = new ArrayList<IPoint>();
		List<IPoint> epsionlonRangedPoints = getEpsilonRangedPoints(point, epsilon);

		int limit = epsionlonRangedPoints.size();
		for (int j = 0; j < limit; j++) {
			IPoint comparePoint = epsionlonRangedPoints.get(j);
			if (MathUtils.distance(comparePoint, point) <= epsilon) {
				neighbors.add(comparePoint);
			}
		}

		return neighbors;
	}

	@Override
	public void onEndClustering() {
		pointsMatrix.addPoints(clearedPoints);
	}

	@Override
	public void onStartClustering() {
		clearedPoints = pointsMatrix.clear();
		pointsMatrix.addPoints(points);
	}
}
