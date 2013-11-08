package de.thm.bi.recognition.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class maps a list of points to a Matrix. Lists can be added to the
 * matrix afterwards or single points. Via setBounds Method the access limits
 * can be set
 * 
 * @author Artur Klos
 * 
 */
public class PointsMatrix {

	private int maxX = 0;

	private int maxY = 0;
	private int minX = 0;

	private int minY = 0;
	private IPoint[][] pointsMatrix;

	/**
	 * Constructor for creating a PointsMatrix out of a PointsMatrix via copy
	 * call
	 * 
	 * @param pointsMatrix
	 */
	private PointsMatrix(IPoint[][] pointsMatrix) {
		this.pointsMatrix = pointsMatrix;
	}

	/**
	 * 
	 * Creates a new PointsMatrix object that has the bounds of the max x and
	 * max y of the from the pointslist. The size of the matrix cannot be raised
	 * afterwards
	 * 
	 * @param points
	 *            List of points
	 */
	public PointsMatrix(List<IPoint> points) {
		setBounds(points);
		pointsMatrix = new IPoint[getWidth()][getHeight()];
		addPoints(points);
	}

	/**
	 * Adds a point to the PointsMatrix
	 * 
	 * @param point
	 *            Point to add
	 */
	public void addPoint(IPoint point) {
		pointsMatrix[point.getX()][point.getY()] = point;
	}

	/**
	 * Adds a list of points to the PointMatrix
	 * 
	 * @param points
	 *            List of points that will be added
	 */
	public void addPoints(List<IPoint> points) {
		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			addPoint(points.get(i));
		}
	}

	/**
	 * @return Clears the array regarding to the current bounds
	 */
	public List<IPoint> clear() {
		ArrayList<IPoint> points = new ArrayList<IPoint>();

		int minX = this.minX, minY = this.minY;
		int maxX = this.maxX, maxY = this.maxY;

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				IPoint point = getPoint(x, y);
				if (point != null) {
					points.add(point);
					removePoint(x, y);
				}
			}
		}

		return points;
	}

	/**
	 * Creates a copy of the current PointsMatrix that uses the same Array for
	 * the new PointsMatrix.
	 * 
	 * @return Copy of this PointsMatrix
	 */
	public PointsMatrix copy() {
		return new PointsMatrix(pointsMatrix);
	}

	/**
	 * @return Gets the height of the PointsMatrix according to its bounds look
	 *         getMinX/Y and getMaxX/Y
	 */
	private int getHeight() {
		return maxY + 1;
	}

	/**
	 * Gets max x
	 * 
	 * @return
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Gets max Y
	 * 
	 * @return
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * @return Returns the current minX
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * @return Gets min y
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets a point from the given coordinates
	 * 
	 * @param x
	 *            Index x
	 * @param y
	 *            Index y
	 * @return
	 */
	public IPoint getPoint(int x, int y) {
		return pointsMatrix[x][y];
	}

	/**
	 * @return Gets the width of the PointsMatrix according to its bounds look
	 *         getMinX/Y and getMaxX/Y
	 */
	private int getWidth() {
		return maxX + 1;
	}

	/**
	 * Removes a point from the given coordinates
	 * 
	 * @param x
	 *            Index x to remove
	 * @param y
	 *            Index y to remove
	 */
	public void removePoint(int x, int y) {
		pointsMatrix[x][y] = null;
	}

	/**
	 * Removes a point from the PointsMatrix
	 * 
	 * @param point
	 *            Point to remove
	 */
	public void removePoint(IPoint point) {
		removePoint(point.getX(), point.getY());
	}

	/**
	 * Removes a List of points form the PointsMatrix
	 * 
	 * @param points
	 *            List to remove from the matrix
	 */
	public void removePoints(List<IPoint> points) {
		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			removePoint(points.get(i));
		}
	}

	/**
	 * Sets the bounds of the PointsMatrix by walking trough the pointlist and
	 * getting the min and max values
	 * 
	 * @param points
	 *            List to analyze
	 */
	public void setBounds(List<IPoint> points) {
		if (points.isEmpty()) {
			return;
		}
		int limit = points.size();
		int maxX = 0;
		int maxY = 0;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;

		for (int i = 0; i < limit; i++) {
			IPoint p = points.get(i);

			int x = p.getX();
			int y = p.getY();

			maxX = Math.max(maxX, x);
			maxY = Math.max(maxY, y);

			minX = Math.min(minX, x);
			minY = Math.min(minY, y);
		}
		this.maxX = maxX;
		this.minX = minX;
		this.minY = minY;
		this.maxY = maxY;
	}

	/**
	 * Sets max x
	 * 
	 * @param maxX
	 */
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	/**
	 * Sets max Y
	 * 
	 * @param maxY
	 */
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	/**
	 * Sets min x
	 * 
	 * @param minX
	 */
	public void setMinX(int minX) {
		this.minX = minX;
	}

	/**
	 * Sets min y
	 * 
	 * @param minY
	 */
	public void setMinY(int minY) {
		this.minY = minY;
	}
}
