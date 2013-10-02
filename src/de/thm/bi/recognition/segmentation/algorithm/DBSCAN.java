package de.thm.bi.recognition.segmentation.algorithm;

import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;

/**
 * @author Artur Klos????????? Implementation of the DBSCAN Algorithm<br>
 *         Epsilon determines the maximum ditsance between points<br>
 *         MinPoints determines the minimum points of a cluster to be not
 *         considered as noise
 */
public class DBSCAN implements ISegmentationAlgorithm {

	private float epsilon;
	private int minPoints;

	/**
	 * Default Constructor that creates a DBSCAN Object with epsilon = 2 and
	 * minPoints = 10??????
	 */
	public DBSCAN() {
		this(2, 10);
	}

	/**
	 * @param epsilon
	 *            The maximum distance between points
	 * @param mintPoints
	 *            Minimum size of a cluster?????????
	 */
	public DBSCAN(float epsilon, int mintPoints) {
		this.epsilon = epsilon;
		this.minPoints = mintPoints;
	}

	private boolean expandCluster(IDataSet dataSet, IPoint point, IRegion cluster, float epsilon, int minPoints) {

		List<IPoint> points = dataSet.getNeighbors(point, epsilon);
		point.setClassification(Classification.CLASSIFIED);
		points.remove(point);
		cluster.addPoint(point);

		while (!points.isEmpty()) {
			IPoint currentPoint = points.remove(0);

			List<IPoint> result = dataSet.getNeighbors(currentPoint, epsilon);

			int limit = result.size();
			for (int i = 0; i < limit; i++) {
				IPoint resultPoint = result.get(i);
				Classification classification = resultPoint.getClassification();
				if (classification == Classification.UNCLASSIFIED || classification == Classification.NOISE) {
					cluster.addPoint(resultPoint);
					resultPoint.setClassification(Classification.CLASSIFIED);
					points.add(resultPoint);
				}
			}
		}

		if (cluster.getAllPoints().size() < minPoints) {
			List<IPoint> allPoints = cluster.getAllPoints();
			int limit = allPoints.size();
			for (int i = 0; i < limit; i++) {
				allPoints.get(i).setClassification(Classification.NOISE);
			}
			allPoints.clear();
			return false;
		}
		return true;

	}

	/**
	 * @return Returns the epsilon value
	 */
	public float getEpsilon() {
		return epsilon;
	}

	/**
	 * @return Returns the current minPoints value
	 */
	public int getMinPoints() {
		return minPoints;
	}

	@Override
	public void performSegmentation(IDataSet dataSet) {
		dataSet.reset();
		dataSet.onStartClustering();
		IRegion cluster = dataSet.createCluster();

		List<IPoint> points = dataSet.getAllPoints();
		int limit = points.size();

		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			if (point.getClassification() == Classification.UNCLASSIFIED) {
				if (expandCluster(dataSet, point, cluster, epsilon, minPoints)) {
					cluster = dataSet.createCluster();
				}
			}
		}
		if (cluster.getAllPoints().size() == 0) {
			dataSet.getClusters().remove(cluster);
		}
		dataSet.onEndClustering();
	}

	/**
	 * @param epsilon
	 *            Sets a new epsilon value
	 */
	public void setEpsilon(float epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * @param minPoints
	 *            Sets a new minPoints value
	 */
	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}

	@Override
	public String toString() {
		return "DBSCAN";
	}

}
