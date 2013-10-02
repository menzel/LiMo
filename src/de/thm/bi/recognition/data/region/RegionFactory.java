package de.thm.bi.recognition.data.region;

import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * Factory for Creating Clusters
 * 
 * @author Artur Klos
 * 
 */
public class RegionFactory {

	public static final int CLUSTER = 0;

	public static int default_cluster = CLUSTER;

	public static final IRegion createCluster(int id, IDataSet parent) {
		switch (id) {
		case CLUSTER:
			return new DefaultRegion(parent);
		}
		return null;
	}

	public static final IRegion createDefault(IDataSet parent) {
		return createCluster(default_cluster, parent);
	}

	private RegionFactory() {
	}
}
