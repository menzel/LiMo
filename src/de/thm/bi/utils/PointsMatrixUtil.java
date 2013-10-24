package de.thm.bi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;
import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * Utility class that uses PointsMatrixes for their algorithms
 * 
 * @author Artur Klos
 * 
 */
public class PointsMatrixUtil {

	public enum BorderSide {
		BORDER_SIDE, E, N, NE, NW, S, SE, SW, W
	}

	/**
	 * Returns the Full Hull of a DataSet. Hull points are points that have no
	 * neighbors on a BorderSide
	 * 
	 * @param dataSet
	 *            DataSet that will be searched
	 * @return Returns the Hull
	 */
	public static final List<IPoint> findFullHull(IDataSet dataSet) {
		return findFullHull(dataSet.toPointsMatrix());
	}

	/**
	 * Returns the Full Hull of a DataSet. Hull points are points that have no
	 * direct neighbor points in one direction. All border points get marked
	 * with the BorderSide Attribute with the AttributeStore interface of the
	 * Points
	 * 
	 * @param pointsMatrix
	 *            That will be searched
	 * @return Returns the Hull
	 */
	public static final List<IPoint> findFullHull(PointsMatrix pointsMatrix) {
		ArrayList<IPoint> border = new ArrayList<>();

		int minX = pointsMatrix.getMinX(), minY = pointsMatrix.getMinY();
		int maxX = pointsMatrix.getMaxX(), maxY = pointsMatrix.getMaxY();

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				if (isBorderPoint(x, y, minX, maxX, minY, maxY, pointsMatrix)) {
					border.add(pointsMatrix.getPoint(x, y));
				}
			}
		}

		return border;
	}

	/**
	 * Gets all points on the line between the points lineStart and lineEnd
	 * 
	 * @param dataSet
	 *            DataSet that will be searched
	 * @param lineStart
	 *            Start point of the line
	 * @param lineEnd
	 *            End point of the line
	 * @return Returns all points that are touching the line
	 */
	public static final List<IPoint> findPointsOnLine(IDataSet dataSet, IPoint lineStart, IPoint lineEnd) {

		if (lineStart.getX() > lineEnd.getX()) {
			IPoint tmp = lineStart;
			lineStart = lineEnd;
			lineEnd = tmp;
		}

		ArrayList<IPoint> points = new ArrayList<>(100);

		PointsMatrix matrix = dataSet.toPointsMatrix();

		int minX = matrix.getMinX();
		int maxX = matrix.getMaxX();

		int minY = matrix.getMinY();
		int maxY = matrix.getMaxY();

		int startX = lineStart.getX();
		int startY = lineStart.getY();

		int endX = lineEnd.getX();
		int endY = lineEnd.getY();

		if (endX - startX == 0) {
			int x = (int) startX;
			for (float currentY = startY; currentY < endY; currentY++) {

				int y = (int) currentY;

				IPoint point = matrix.getPoint(x, y);
				if (point != null && !points.contains(point)) {
					points.add(point);
				}

				if (x - 1 >= minX) {
					// LEFT POINT
					point = matrix.getPoint(x - 1, y);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}
				if (x + 1 <= maxX) {
					// RIGHT POINT
					point = matrix.getPoint(x + 1, y);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}

				if (y - 1 >= minY) {
					// TOP POINT
					point = matrix.getPoint(x, y - 1);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}

				if (y + 1 <= maxY) {
					// BOT POINT
					point = matrix.getPoint(x, y + 1);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}

				if (x - 1 >= minX && y - 1 >= minY) {
					// TOPLEFT POINT
					point = matrix.getPoint(x - 1, y - 1);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}
				if (x + 1 <= maxX && y - 1 >= minY) {
					// TOPRIGHT POINT
					point = matrix.getPoint(x + 1, y - 1);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}

				if (x - 1 >= minX && y + 1 <= maxY) {
					// BOTLEFT POINT
					point = matrix.getPoint(x - 1, y + 1);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}

				if (x + 1 <= maxX && y + 1 <= maxY) {
					// BOTRIGHT POINT
					point = matrix.getPoint(x + 1, y + 1);
					if (point != null && !points.contains(point)) {
						points.add(point);
					}
				}

			}
			return points;
		}

		float slope = (endY - startY) / (endX - startX);

		float xStepSize = Math.max(1, 1 / slope);

		for (float currentX = startX, currentY = startY; currentX < endX || currentY < endY; currentX += xStepSize, currentY += slope
				* xStepSize) {
			int x = (int) currentX;
			int y = (int) currentY;

			IPoint point = matrix.getPoint(x, y);
			if (point != null) {
				points.add(point);
			}

			if (x - 1 >= minX) {
				// LEFT POINT
				point = matrix.getPoint(x - 1, y);
				if (point != null) {
					points.add(point);
				}
			}
			if (x + 1 <= maxX) {
				// RIGHT POINT
				point = matrix.getPoint(x + 1, y);
				if (point != null) {
					points.add(point);
				}
			}

			if (y - 1 >= minY) {
				// TOP POINT
				point = matrix.getPoint(x, y - 1);
				if (point != null) {
					points.add(point);
				}
			}

			if (y + 1 <= maxY) {
				// BOT POINT
				point = matrix.getPoint(x, y + 1);
				if (point != null) {
					points.add(point);
				}
			}

			if (x - 1 >= minX && y - 1 >= minY) {
				// TOPLEFT POINT
				point = matrix.getPoint(x - 1, y - 1);
				if (point != null) {
					points.add(point);
				}
			}
			if (x + 1 <= maxX && y - 1 >= minY) {
				// TOPRIGHT POINT
				point = matrix.getPoint(x + 1, y - 1);
				if (point != null) {
					points.add(point);
				}
			}

			if (x - 1 >= minX && y + 1 <= maxY) {
				// BOTLEFT POINT
				point = matrix.getPoint(x - 1, y + 1);
				if (point != null) {
					points.add(point);
				}
			}

			if (x + 1 <= maxX && y + 1 <= maxY) {
				// BOTRIGHT POINT
				point = matrix.getPoint(x + 1, y + 1);
				if (point != null) {
					points.add(point);
				}
			}

		}

		return points;
	}

	/**
	 * Gets the HullPoint on the other Side of the Hull relative compared to the
	 * passed point
	 * 
	 * @param dataSet
	 *            Dataset that will be searched
	 * @param point
	 *            Point to whom the counter point should be searched
	 * @return Returns the Hull point on the other side of the Hull comapred to
	 *         the passed point
	 */
	public static final IPoint getCounterHullPoint(IDataSet dataSet, IPoint point) {
		// TODO : die suche des counter punktes anpassen... den punkt in
		// mehreren richtungen suchen z.b wenn n dann s sw und se suchen auf die
		// gefundenen punkte mittels neuem dataset eine neighbor suche machen
		// und den n��hsten aus der liste suchen so kann man wahrscheinlich
		// einen
		// besseren counterpoint finden als stumpf in eine richtung zu gehen
		BorderSide side = (BorderSide) point.getStoredAttribute(BorderSide.BORDER_SIDE);
		if (side == null) {
			return null;
		}

		int xStep = 0;
		int yStep = 0;

		switch (side) {
		case N:
			yStep = 1;
			break;
		case NE:
			xStep = -1;
			yStep = 1;
			break;
		case E:
			xStep = -1;
		case SE:
			yStep = -1;
			xStep = -1;
			break;
		case S:
			yStep = -1;
			break;
		case SW:
			yStep = -1;
			xStep = 1;
			break;
		case W:
			xStep = 1;
			break;
		case NW:
			yStep = 1;
			xStep = 1;
			break;
		default:
		}

		PointsMatrix matrix = dataSet.toPointsMatrix();

		int minX = matrix.getMinX();
		int maxX = matrix.getMaxX();

		int minY = matrix.getMinY();
		int maxY = matrix.getMaxY();

		int x = point.getX(), y = point.getY();
		int numberOfStepsWithoutPoint = 0;

		IPoint counterBorder = point;

		while (x > minX && x < maxX && y > minY && y < maxY && numberOfStepsWithoutPoint < 1) {
			IPoint possibleBorder = matrix.getPoint(x, y);
			if (possibleBorder == null) {
				numberOfStepsWithoutPoint++;
			} else {
				counterBorder = possibleBorder;
				numberOfStepsWithoutPoint = 0;
			}
			x += xStep;
			y += yStep;

		}
		return counterBorder;
	}

	/**
	 * Checks weather the point on coordinate x and y is a border point or not.
	 * If the this point is border point it will be marked via the BorderSide
	 * attribute.
	 * 
	 * @param x
	 *            x coordinate of the point to check
	 * @param y
	 *            y coordinate of the point to check
	 * @param minX
	 *            the minX value of the pointsMatrix
	 * @param maxX
	 *            the maxX value of the pointsMatrix
	 * @param minY
	 *            the minY value of the pointsMatrix
	 * @param maxY
	 *            the maxX value f the pointsMatrix
	 * @param pointsMatrix
	 *            PointsMatrix that contains all points
	 * @return Returns true if on one side of this Point is no other Point. Th
	 */
	public static boolean isBorderPoint(int x, int y, int minX, int maxX, int minY, int maxY, PointsMatrix pointsMatrix) {
		IPoint point = pointsMatrix.getPoint(x, y);
		if (point == null) {
			return false;
		}

		if (x == minX || x == maxX) {
			return true;
		}
		if (y == minY || y == maxY) {
			return true;
		}
		// LEFT POINT
		IPoint comparePoint = pointsMatrix.getPoint(x - 1, y);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.W);
			return true;
		}
		// RIGHT POINT
		comparePoint = pointsMatrix.getPoint(x + 1, y);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.E);
			return true;
		}

		// TOP POINT
		comparePoint = pointsMatrix.getPoint(x, y - 1);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.N);
			return true;
		}

		// BOT POINT
		comparePoint = pointsMatrix.getPoint(x, y + 1);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.S);
			return true;
		}

		// TOPLEFT POINT
		comparePoint = pointsMatrix.getPoint(x - 1, y - 1);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.NW);
			return true;
		}

		// TOPRIGHT POINT
		comparePoint = pointsMatrix.getPoint(x + 1, y - 1);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.NE);
			return true;
		}

		// BOTLEFT POINT
		comparePoint = pointsMatrix.getPoint(x - 1, y + 1);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.SW);
			return true;
		}

		// BOTRIGHT POINT
		comparePoint = pointsMatrix.getPoint(x + 1, y + 1);
		if (comparePoint == null) {
			point.storeAttribute(BorderSide.BORDER_SIDE, BorderSide.SE);
			return true;
		}

		return false;

	}

	// Each point passed in via the "points" array should be unique.
	// If duplicates are passed in the returned polygon might not be a convex
	// hull.
	public static final List<IPoint> monotoneChainHull(List<IPoint> points) {
		int n = points.size();
		Collections.sort(points);
		IPoint[] ans = new IPoint[2 * n]; // In between we may have a 2n points
		int k = 0;
		int start = 0; // start is the first insertion point

		for (int i = 0; i < n; i++) // Finding lower layer of hull
		{
			IPoint p = points.get(i);
			while (k - start >= 2 && p.sub(ans[k - 1]).cross(p.sub(ans[k - 2])) > 0)
				k--;
			ans[k++] = p;
		}

		k--; // drop off last point from lower layer
		start = k;

		for (int i = n - 1; i >= 0; i--) // Finding top layer from hull
		{
			IPoint p = points.get(i);
			while (k - start >= 2 && p.sub(ans[k - 1]).cross(p.sub(ans[k - 2])) > 0)
				k--;
			ans[k++] = p;
		}
		k--; // drop off last point from top layer

		return Arrays.asList(Arrays.copyOf(ans, k)); // convex hull is of size k
	}
}
