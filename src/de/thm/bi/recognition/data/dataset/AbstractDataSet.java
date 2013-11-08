package de.thm.bi.recognition.data.dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.Point;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.data.region.RegionFactory;
import de.thm.bi.recognition.filter.IPointFilter;
import de.thm.bi.utils.AttributeStore;
import de.thm.bi.utils.PointsMatrixUtil;

/**
 * Abstract implementation of the IDataSet
 * 
 * @author Artur Klos
 * 
 */
public abstract class AbstractDataSet implements IDataSet {

	private static final Comparator<IRegion> clusterSizeComparator = new Comparator<IRegion>() {
		@Override
		public int compare(IRegion o1, IRegion o2) {
			return o1.size() - o2.size();
		}

	};

	private AttributeStore attributeStore = new AttributeStore();
	protected IPoint centroid;
	protected boolean centroidValid = false;
	protected List<IRegion> clusters = new ArrayList<IRegion>();

	protected List<IPoint> points;
	protected PointsMatrix pointsMatrix;

	protected boolean pointsMatrixValid = false;

	public AbstractDataSet(List<IPoint> points) {
		this.points = points;
		pointsMatrix = new PointsMatrix(points);
	}

	public AbstractDataSet(List<IPoint> points, PointsMatrix pointsMatrix) {
		this.points = points;
		this.pointsMatrix = pointsMatrix;
	}

	@Override
	public IRegion createCluster() {
		IRegion cluster = RegionFactory.createDefault(this);
		clusters.add(cluster);
		return cluster;
	}

	@Override
	public void dataSetChanged() {
	}

	@Override
	public List<IPoint> erodeConvexHull() {
		List<IPoint> border = getConvexHull();
		points.removeAll(border);
		pointsMatrix.removePoints(border);
		return border;
	}

	@Override
	public List<IPoint> erodeHull() {
		List<IPoint> border = getFullHull();
		points.removeAll(border);
		pointsMatrix.removePoints(border);
		return border;
	}

	@Override
	public List<IPoint> filter(IPointFilter filter) {
		List<IPoint> filteredPoints = filter.filter(this);
		points.removeAll(filteredPoints);
		pointsMatrix.removePoints(filteredPoints);
		return filteredPoints;
	}

	@Override
	public List<IPoint> getAllPoints() {
		return points;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.dataset.IDataSet#getAverageClusterSize()
	 */
	@Override
	public int getAverageClusterSize() {
		int averageSize = 0;
		List<IRegion> clusters = this.clusters;
		int limit = clusters.size();
		for (int i = 0; i < limit; i++) {
			IRegion cluster = clusters.get(i);
			averageSize += cluster.getAllPoints().size();
		}
		if (limit != 0)
			averageSize /= limit;
		return averageSize;
	}

	@Override
	public IPoint getCentroid() {

		if (!centroidValid) {
			int limit = points.size();
			if (limit != 0) {
				int x = 0;
				int y = 0;
				for (int i = 0; i < limit; i++) {
					IPoint point = points.get(i);
					x += point.getX();
					y += point.getY();
				}
				x /= limit;
				y /= limit;

				centroid = new Point(x, y);
				centroidValid = true;
			}
		}

		return centroid;
	}

	@Override
	public List<IRegion> getClusters() {
		return clusters;
	}

	@Override
	public List<IPoint> getConvexHull() {
		return PointsMatrixUtil.monotoneChainHull(points);
	}

	@Override
	public List<IPoint> getFullHull() {
		List<IPoint> clearedPoints = pointsMatrix.clear();
		pointsMatrix.addPoints(this.points);
		List<IPoint> border = PointsMatrixUtil.findFullHull(this);
		pointsMatrix.addPoints(clearedPoints);
		return border;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.dataset.IDataSet#getMedianClusterSize()
	 */
	@Override
	public int getMedianClusterSize() {
		Collections.sort(clusters, clusterSizeComparator);
		return clusters.get((int) (clusters.size() * 0.5)).size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.dataset.IDataSet#getPointByClassification
	 * (dbpcanalyzer.ak.clustering.data.IPoint.Classification)
	 */
	@Override
	public List<IPoint> getPointsByClassification(Classification classification) {
		ArrayList<IPoint> result = new ArrayList<IPoint>(250);
		List<IPoint> points = this.points;
		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			if (point.getClassification() == classification) {
				result.add(point);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#getStoredAttribute(java.lang.Object
	 * )
	 */
	@Override
	public Object getStoredAttribute(Object attributeName) {
		return attributeStore.getStoredAttribute(attributeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#hasStoredAttribute(java.lang.Object
	 * )
	 */
	@Override
	public boolean hasStoredAttribute(Object attributeName) {
		return attributeStore.hasStoredAttribute(attributeName);
	}

	@Override
	public void invalidateCentroid() {
		centroidValid = false;
	}

	@Override
	public void invalidatePointsMatrix() {
		pointsMatrix.setBounds(points);
		pointsMatrix.addPoints(points);
	}

	@Override
	public void onEndClustering() {
	}

	@Override
	public void onStartClustering() {
	}

	@Override
	public void reset() {
		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			point.setClassification(Classification.UNCLASSIFIED);
		}
		clusters.clear();
	}

	@Override
	public int size() {
		return points.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#storeAttribute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void storeAttribute(Object attributeName, Object object) {
		attributeStore.storeAttribute(attributeName, object);
	}

	@Override
	public PointsMatrix toPointsMatrix() {
		return pointsMatrix;
	}
}
