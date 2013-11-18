/**
 * 
 */
package de.thm.bi.recognition.segmentation.algorithm;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.utils.Vector3D;
import de.thm.bi.utils.Vector3DPool;

/**
 * @author Artur Klos
 * 
 */
public class ColorSegmentation implements ISegmentationAlgorithm {

	public float maxColorDistance = 40;
	private DBSCAN dbscan = new DBSCAN(2, 5);

	@Override
	public void performSegmentation(IDataSet dataSet) {
		dataSet.reset();
		dataSet.onStartClustering();
		dbscan.performSegmentation(dataSet);
		List<IRegion> dataSetRegions = dataSet.getClusters();
		List<IRegion> regions = new ArrayList<>(dataSetRegions);
		dataSetRegions.clear();
		int limit = regions.size();
		int counter = 0;
		int numberOfColors = 0;

		dataSetRegions.add(regions.remove(0));
		while (!regions.isEmpty()) {
			System.out.println("ALL: " + limit);
			System.out.println("CUR: " + (counter++));

			IRegion region = regions.remove(0);
			int dataSetLimit = dataSetRegions.size();
			boolean noMatch = true;

			for (int i = 0; i < dataSetLimit; i++) {
				IRegion compareRegion = dataSetRegions.get(i);

				Vector3D regionColor = getMiddleRegionColor(region);
				Vector3D compageRegionColor = getMiddleRegionColor(compareRegion);
				float dist = Vector3D.distance(regionColor, compageRegionColor);
				if (dist < maxColorDistance) {
					compareRegion.addAll(region.getAllPoints());
					noMatch = false;
					break;
				}
				Vector3DPool.recycle(regionColor);
				Vector3DPool.recycle(compageRegionColor);
			}
			if (noMatch) {
				dataSetRegions.add(region);
				System.out.println("COLORS: " + numberOfColors++);
			}
		}

		dataSet.onEndClustering();
	}

	public Vector3D getMiddleRegionColor(IRegion region) {
		Vector3D colorvec = Vector3DPool.obtain(0, 0, 0);

		List<IPoint> points = region.getAllPoints();
		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			Vector3D vec = Vector3DPool.obtain(point);
			colorvec.add(vec);
			Vector3DPool.recycle(vec);
		}
		if (limit != 0) {
			colorvec.div(limit);
		}
		return colorvec;
	}
}
