package de.thm.bi.recognition.data.dataset;

import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.recognition.data.IPoint.Classification;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.filter.IPointFilter;
import de.thm.bi.utils.IAttributeStore;

/**
 * Describes methods for a DataSet for Clustering Algorithms
 * 
 * @author Artur Klos
 * 
 */
public interface IDataSet extends IAttributeStore {

	/**
	 * @return Creates and return a new Cluster
	 */
	public IRegion createCluster();

	/**
	 * Tells the DataSet that the dataSet has changed
	 */
	public void dataSetChanged();

	/**
	 * Erodes the ConvexHull
	 * 
	 * @return Removes the Border of the cluster
	 */
	public List<IPoint> erodeConvexHull();

	/**
	 * @return Returns the eroded Hull
	 */
	public List<IPoint> erodeHull();

	/**
	 * Filters all points with Filter
	 * 
	 * @param filter
	 * @return Filtered Points
	 */
	public List<IPoint> filter(IPointFilter filter);

	/**
	 * @return Returns all points hold by the DataSet
	 */
	public List<IPoint> getAllPoints();

	/**
	 * @return Returns the average Cluster size
	 */
	public int getAverageClusterSize();

	/**
	 * @return Returns the Centroid of this Cluster
	 */
	public IPoint getCentroid();

	/**
	 * If DataSet has been Clustered this Method returns a List of Clusters.
	 * 
	 * @return List of Clusters
	 */
	public List<IRegion> getClusters();

	/**
	 * @return Returns the Border of the Cluster
	 */
	public List<IPoint> getConvexHull();

	/**
	 * @return Returns the Hull of the Cluster
	 */
	public List<IPoint> getFullHull();

	/**
	 * @return Returns the median cluster size
	 */
	public int getMedianClusterSize();

	/**
	 * @param point
	 *            Reference Point to search the region
	 * @param epsilon
	 *            Distance that will be recognized as neighbour
	 * @return List of Points that
	 */
	public List<IPoint> getNeighbors(IPoint point, float epsilon);

	/**
	 * @param classification
	 *            Classification to search for
	 * @return Returns a list of points that matches the classification
	 */
	public List<IPoint> getPointsByClassification(Classification classification);

	/**
	 * Invalidates the current centroid so that a new one will be calculated on
	 * getCentroid Call
	 */
	public void invalidateCentroid();

	/**
	 * Tells the DataSet that the pointsMatrix has to be recreated on to
	 * pointsMatrixCall
	 */
	public void invalidatePointsMatrix();

	/**
	 * Is called on clustering finished
	 */
	public void onEndClustering();

	/**
	 * Is called on clustering start
	 */
	public void onStartClustering();

	/**
	 * Sets all points to unclassified
	 */
	public void reset();

	/**
	 * @return Returns ne number of points in this dataSet
	 */
	public int size();

	/**
	 * @return Returns a pointarray out of the dataSet
	 */
	public PointsMatrix toPointsMatrix();
}
