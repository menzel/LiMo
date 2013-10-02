package de.thm.bi.recognition.data.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.dataset.DataSetFactory;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.filter.IPointFilter;
import de.thm.bi.utils.AttributeStore;
import de.thm.bi.utils.ID;
import de.thm.bi.utils.IDFactory;

/**
 * Default Implementation of the Cluster Interface
 * 
 * @author Artur Klos
 * 
 */
public class DefaultRegion implements IRegion {

	private AttributeStore attributeStore = new AttributeStore();
	private IDataSet dataSet;
	private ID id = IDFactory.createApplicationUniqueId();
	private IDataSet parent;

	/**
	 * Creates a new DefaultCluster Object
	 * 
	 * @param parent
	 *            The DataSet that created the cluster
	 */
	public DefaultRegion(IDataSet parent) {
		this.parent = parent;
		dataSet = DataSetFactory.createDefault(new ArrayList<IPoint>(100), parent.toPointsMatrix().copy());
	}

	@Override
	public void addAll(Collection<IPoint> collection) {
		dataSet.getAllPoints().addAll(collection);
		int limit = dataSet.getAllPoints().size();
		for (int i = 0; i < limit; i++) {
			dataSet.getAllPoints().get(i).setClassification(Classification.CLASSIFIED);
		}
	}

	@Override
	public void addPoint(IPoint point) {
		dataSet.getAllPoints().add(point);
		point.setClassification(Classification.CLASSIFIED);
	}

	@Override
	public IRegion createCluster() {
		return dataSet.createCluster();
	}

	@Override
	public void dataSetChanged() {
		dataSet.dataSetChanged();
	}

	@Override
	public List<IPoint> erodeConvexHull() {
		return dataSet.erodeConvexHull();
	}

	@Override
	public List<IPoint> erodeHull() {
		return dataSet.erodeHull();
	}

	@Override
	public List<IPoint> filter(IPointFilter filter) {
		return dataSet.filter(filter);
	}

	@Override
	public List<IPoint> getAllPoints() {
		return dataSet.getAllPoints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.dataset.IDataSet#getAverageClusterSize()
	 */
	@Override
	public int getAverageClusterSize() {
		return dataSet.getAverageClusterSize();
	}

	@Override
	public IPoint getCentroid() {
		return dataSet.getCentroid();
	}

	@Override
	public List<IRegion> getClusters() {
		return dataSet.getClusters();
	}

	@Override
	public List<IPoint> getConvexHull() {
		return dataSet.getConvexHull();
	}

	@Override
	public List<IPoint> getFullHull() {
		return dataSet.getFullHull();
	}

	@Override
	public long getId() {
		return id.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.dataset.IDataSet#getMedianClusterSize()
	 */
	@Override
	public int getMedianClusterSize() {
		return dataSet.getMedianClusterSize();
	}

	@Override
	public List<IPoint> getNeighbors(IPoint point, float epsilon) {
		return dataSet.getNeighbors(point, epsilon);
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
		return dataSet.getPointsByClassification(classification);
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
		dataSet.invalidateCentroid();
	}

	@Override
	public void invalidatePointsMatrix() {
		dataSet.invalidatePointsMatrix();
	}

	@Override
	public void onEndClustering() {
		dataSet.onEndClustering();
	};

	@Override
	public void onStartClustering() {
		dataSet.onStartClustering();
	}

	@Override
	public void reset() {
		dataSet.reset();
	}

	@Override
	public int size() {
		return dataSet.size();
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
		return dataSet.toPointsMatrix();
	}
}
