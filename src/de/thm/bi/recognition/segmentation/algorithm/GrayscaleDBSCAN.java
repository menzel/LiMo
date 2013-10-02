/**
 * 
 */
package de.thm.bi.recognition.segmentation.algorithm;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.normalize.INormalization.Attributes;
import de.thm.bi.utils.MathUtils;
import de.thm.bi.utils.Preferences;

/**
 * @author Artur Klos
 * 
 */
public class GrayscaleDBSCAN implements ISegmentationAlgorithm {

	private float epsilon;
	private boolean isAdjustMaxGrayscaleEnabled = true;
	private int maxPoints;
	private int maxGrayscale;

	private float sizeVariance = 0.05f;
	private SizeSource sizeSource = SizeSource.MEDIAN;

	private enum GSDBSCANAttributes {
		MAX_CLUSTER_SIZE
	}

	/**
	 * Default Constructor that creates a GrayScaleDBSCAN Object with epsilon =
	 * 2 and minPoints = 10??????
	 */
	public GrayscaleDBSCAN() {
		this(2, 10, 255);
	}

	/**
	 * @param epsilon
	 *            The maximum distance between points
	 * @param mintPoints
	 *            Minimum size of a cluster?????????
	 * @param maxGrayscale
	 *            m
	 */
	public GrayscaleDBSCAN(float epsilon, int mintPoints, int maxGrayscale) {
		this.epsilon = epsilon;
		this.maxPoints = mintPoints;
		this.maxGrayscale = maxGrayscale;
	}

	/**
	 * Attaches points to the nearest cluster found not regarding the epsilon
	 * distance
	 */
	private void attachPointsToNearestCluster(List<IPoint> points, IDataSet dataSet) {

		// TODO: Verbessern sodass die punkte in der epsilon nachbarschaft sind
		// und wirklich dem richtigen cluster hinzugef??????gt werden
		List<IRegion> clusters = dataSet.getClusters();

		IRegion mindistanceCluster = null;
		float mindistance = Float.MAX_VALUE;

		int limit = points.size();
		int clusterLimit = clusters.size();

		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			for (int j = 0; j < clusterLimit; j++) {
				IRegion cluster = clusters.get(j);
				IPoint centroid = cluster.getCentroid();
				float distance = MathUtils.squaredDistance(point, centroid);
				if (distance < mindistance) {
					mindistanceCluster = cluster;
					mindistance = distance;
				}
			}
			mindistanceCluster.addPoint(point);
			mindistance = Float.MAX_VALUE;
		}

		for (int i = 0; i < clusterLimit; i++) {
			clusters.get(i).invalidatePointsMatrix();
		}
	}

	public void doPerformClustering(IDataSet dataSet, int maxGrayscale) {
		dataSet.reset();
		dataSet.onStartClustering();
		IRegion cluster = dataSet.createCluster();
		cluster.storeAttribute(GSDBSCANAttributes.MAX_CLUSTER_SIZE, maxGrayscale);

		List<IPoint> points = dataSet.getAllPoints();
		int limit = points.size();

		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			if (point.getClassification() == Classification.UNCLASSIFIED) {
				if (expandCluster(dataSet, point, cluster, epsilon, maxPoints, maxGrayscale)) {
					cluster = dataSet.createCluster();
					cluster.storeAttribute(GSDBSCANAttributes.MAX_CLUSTER_SIZE, maxGrayscale);
				}
			}
		}

		if (cluster.getAllPoints().size() == 0) {
			dataSet.getClusters().remove(cluster);
		}
		dataSet.onEndClustering();
	}

	private boolean expandCluster(IDataSet dataSet, IPoint point, IRegion cluster, float epsilon, int minPoints,
			int maxGrayscale) {

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
					if ((int) resultPoint.getStoredAttribute(Attributes.GRAYSCALE_INT) <= maxGrayscale) {
						cluster.addPoint(resultPoint);
						resultPoint.setClassification(Classification.CLASSIFIED);
						points.add(resultPoint);
					} else {
						resultPoint.setClassification(Classification.UNCLASSIFIED);
					}
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
	 * @return the minGrayscale
	 */
	public int getMaxGrayscale() {
		return maxGrayscale;
	}

	/**
	 * @return Returns the current minPoints value
	 */
	public int getMinPoints() {
		return maxPoints;
	}

	private int getSize(IDataSet dataSet) {
		switch (sizeSource) {
		case AVERAGE:
			return dataSet.getAverageClusterSize();
		case MEDIAN:
			return dataSet.getMedianClusterSize();
		case PRESET:
			return Preferences.getDefaultClusterSize();
		default:
			return 0;
		}
	}

	/**
	 * @param sizeSource
	 *            the sizeSource to set
	 */
	public void setSizeSource(SizeSource sizeSource) {
		this.sizeSource = sizeSource;
	}

	/**
	 * @return Gets the current size variance
	 */
	public float getSizeVariance() {
		return sizeVariance;
	}

	/**
	 * @return the isAdjustMinGrayscaleEnabled
	 */
	public boolean isAdjustMaxGrayscaleEnabled() {
		return isAdjustMaxGrayscaleEnabled;
	}

	@Override
	public void performSegmentation(IDataSet dataSet) {
		doPerformClustering(dataSet, maxGrayscale);

		if (isAdjustMaxGrayscaleEnabled) {
			subCluster(dataSet);
		}
	}

	/**
	 * @param isAdjustMinGrayscaleEnabled
	 *            the isAdjustMinGrayscaleEnabled to set
	 */
	public void setAdjustMaxGrayscaleEnabled(boolean isAdjustMinGrayscaleEnabled) {
		this.isAdjustMaxGrayscaleEnabled = isAdjustMinGrayscaleEnabled;
	}

	/**
	 * @param epsilon
	 *            Sets a new epsilon value
	 */
	public void setEpsilon(float epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * @param minGrayscale
	 *            the minGrayscale to set
	 */
	public void setMaxGrayscale(int minGrayscale) {
		this.maxGrayscale = minGrayscale;
	}

	/**
	 * @param minPoints
	 *            Sets a new minPoints value
	 */
	public void setMinPoints(int minPoints) {
		this.maxPoints = minPoints;
	}

	/**
	 * Sets the size variance in percent for example 0.2f means the size might
	 * be 20 percent higher then the middle size
	 * 
	 * @param sizeVariance
	 * 
	 */
	public void setSizeVariance(float sizeVariance) {
		this.sizeVariance = sizeVariance;
	}

	/**
	 * Performs a subClustering on the passed DataSet
	 * 
	 * @param dataSet
	 *            DataSet that will be sub clustered
	 */
	public void subCluster(final IDataSet dataSet) {

		List<IRegion> dsClusters = dataSet.getClusters();
		List<IRegion> tmpClusters = new ArrayList<>(dsClusters.size() * 2);

		int maxClusterSize = (int) (getSize(dataSet) * (sizeVariance + 1f));

		while (!dsClusters.isEmpty()) {
			IRegion cluster = dsClusters.remove(0);
			if (cluster.size() > maxClusterSize) {
				int grayscale = (int) cluster.getStoredAttribute(GSDBSCANAttributes.MAX_CLUSTER_SIZE) - 1;
				cluster.dataSetChanged();
				doPerformClustering(cluster, grayscale);
				dsClusters.addAll(cluster.getClusters());
			} else {
				tmpClusters.add(cluster);
			}
		}
		dsClusters.addAll(tmpClusters);

		List<IPoint> filteredPoints = dataSet.getPointsByClassification(Classification.UNCLASSIFIED);
		filteredPoints.addAll(dataSet.getPointsByClassification(Classification.NOISE));

		// attachPointsToNearestCluster(filteredPoints, dataSet);
		while (!dsClusters.isEmpty()) {
			IRegion cluster = dsClusters.remove(0);
			cluster.dataSetChanged();
			doPerformClustering(cluster, maxGrayscale);
			tmpClusters.addAll(cluster.getClusters());
		}
		dsClusters.addAll(tmpClusters);
	}

	@Override
	public String toString() {
		return "GrayscaleDBSCAN";
	}
}
