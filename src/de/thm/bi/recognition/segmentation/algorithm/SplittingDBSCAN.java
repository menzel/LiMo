package de.thm.bi.recognition.segmentation.algorithm;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.filter.IPointFilter;
import de.thm.bi.recognition.filter.PointFilterFactory;
import de.thm.bi.utils.MathUtils;
import de.thm.bi.utils.Preferences;

/**
 * A extension of the normal DBSCAN that tries to split Clusters that exceed a
 * certain variance of the average or median cluster size. To split the Clusters
 * the Default IPointFilter provided by the PointFilterFactory is used. The
 * filter applies on the large clusters and a clustering on the filtered points
 * is performed. This the filter will be applied till the sub clusters have the
 * desired size
 * 
 * @author Artur Klos
 */
public class SplittingDBSCAN extends DBSCAN {

	private IPointFilter filter = PointFilterFactory.createDefault();
	private float sizeVariance = 0.05f;
	private SizeSource sizeSource = SizeSource.MEDIAN;

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

	/**
	 * @return Gets the current size variance
	 */
	public float getSizeVariance() {
		return sizeVariance;
	}

	@Override
	public void performSegmentation(IDataSet dataSet) {
		super.performSegmentation(dataSet);
		subCluster(dataSet);
	}

	/**
	 * @param filter
	 *            Sets the filter that is used to split clusters
	 */
	public void setFilter(IPointFilter filter) {
		this.filter = filter;
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
	 * Performs a subClustering on the passed DataSet
	 * 
	 * @param dataSet
	 *            DataSet that will be sub clustered
	 */
	public void subCluster(final IDataSet dataSet) {

		List<IRegion> dsClusters = dataSet.getClusters();
		List<IRegion> tmpClusters = new ArrayList<>(dsClusters.size() * 2);
		List<IPoint> filteredPoints = new ArrayList<>(10000);

		int maxClusterSize = (int) (getSize(dataSet) * (sizeVariance + 1f));

		while (!dsClusters.isEmpty()) {
			IRegion cluster = dsClusters.remove(0);
			if (cluster.size() > maxClusterSize) {
				cluster.dataSetChanged();
				filteredPoints.addAll(cluster.filter(filter));
				cluster.dataSetChanged();
				super.performSegmentation(cluster);
				dsClusters.addAll(cluster.getClusters());
			} else {
				tmpClusters.add(cluster);
			}
		}
		dsClusters.addAll(tmpClusters);

		attachPointsToNearestCluster(filteredPoints, dataSet);
		while (!dsClusters.isEmpty()) {
			IRegion cluster = dsClusters.remove(0);
			cluster.dataSetChanged();
			super.performSegmentation(cluster);
			tmpClusters.addAll(cluster.getClusters());
		}
		dsClusters.addAll(tmpClusters);
	}
}
