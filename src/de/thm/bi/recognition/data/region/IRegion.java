package de.thm.bi.recognition.data.region;

import java.util.Collection;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.utils.IAttributeStore;

/**
 * Interface that describes methods for Clusters
 * 
 * @author Artur Klos
 * 
 */
public interface IRegion extends IDataSet, IAttributeStore {
	/**
	 * @param collection
	 *            Adds a collection to the cluster
	 */
	public void addAll(Collection<IPoint> collection);

	/**
	 * @param point
	 *            Point that should be added to the Cluster
	 */
	public void addPoint(IPoint point);

	/**
	 * @return Retruns a Unique ID
	 */
	public long getId();

}
