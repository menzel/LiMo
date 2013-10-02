package de.thm.bi.recognition.data.dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.utils.MathUtils;

/**
 * A implementation of the IDataSet interface that precalculates the distances
 * to all Points and saves all Points that have a epsilon below 10. Afterwards
 * the determination of epsilon neighborhood is very fast but much heapspace has
 * to be allocated
 * 
 * @author Artur Klos
 * 
 */
public class DistanceList extends AbstractDataSet {

	private class PointDistanceContainer {
		public final float distance;
		public final IPoint point;

		public PointDistanceContainer(float distance, IPoint point) {
			this.distance = distance;
			this.point = point;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PointDistanceContainer) {
				PointDistanceContainer container = (PointDistanceContainer) obj;
				return container.point == this.point && container.distance == container.distance;
			}
			return false;
		}
	}

	private static final String STORE_ATTRIBUTE_DISTANCE_LIST = "distance_list";

	DistanceList(List<IPoint> points) {
		super(points);
		calculateDistanceLists();
	}

	public DistanceList(List<IPoint> points, PointsMatrix pointsMatrix) {
		super(points, pointsMatrix);
	}

	private void calculateDistanceLists() {
		int limit = points.size();
		List<IPoint> points = this.points;
		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			List<PointDistanceContainer> distanceList = new ArrayList<PointDistanceContainer>(500);
			for (int j = 0; j < limit; j++) {
				IPoint comaprePoint = points.get(j);
				float distance = MathUtils.distance(point.getX(), point.getY(), comaprePoint.getX(),
						comaprePoint.getY());
				if (distance <= 10) {
					distanceList.add(new PointDistanceContainer(distance, comaprePoint));
				}

			}
			Collections.sort(distanceList, new Comparator<PointDistanceContainer>() {

				@Override
				public int compare(PointDistanceContainer o1, PointDistanceContainer o2) {

					if (o1.distance < o2.distance) {
						return 1;
					} else if (o1.distance > o2.distance) {
						return -1;
					}
					return 0;
				}
			});
			point.storeAttribute(STORE_ATTRIBUTE_DISTANCE_LIST, distanceList);
		}
	}

	@Override
	public List<IPoint> getNeighbors(IPoint point, float epsilon) {
		@SuppressWarnings("unchecked")
		List<PointDistanceContainer> distanceList = (List<PointDistanceContainer>) point
				.getStoredAttribute(STORE_ATTRIBUTE_DISTANCE_LIST);

		List<IPoint> resulList = new ArrayList<IPoint>();
		int limit = distanceList.size();
		for (int i = 0; i < limit; i++) {
			PointDistanceContainer container = distanceList.get(i);
			if (container.distance <= epsilon) {
				resulList.add(container.point);
			}
		}
		return resulList;
	}
}
