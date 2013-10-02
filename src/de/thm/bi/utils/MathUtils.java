package de.thm.bi.utils;

import java.util.Random;

import de.thm.bi.recognition.data.IPoint;

/**
 * Utility class for mathematical
 * 
 * @author Artur Klos
 * 
 */
public class MathUtils {

	public static final Random RANDOM = new Random(System.nanoTime());

	/**
	 * Calculates the curvature between point one and two
	 * 
	 * @param x1
	 *            x coordinate of point one
	 * @param y1
	 *            y coordinate of point one
	 * @param x2
	 *            x coordinate of point two
	 * @param y2
	 *            y coordinate of point two
	 * @return Returns the curvature between two pointss
	 */
	public static final float calculateCurvature(float x1, float y1, float x2, float y2) {
		return (float) ((x1 * y2 - y1 * x2) / (Math.pow(x1 * x1 + y1 * y1, 1.5)));
	}

	/**
	 * Calculates the curvature between point one and two
	 * 
	 * @param one
	 *            Point one
	 * @param two
	 *            Point two
	 * @return Returns the curvature between two points
	 */
	public static final float calculateCurvature(IPoint one, IPoint two) {
		return calculateCurvature(one.getX(), one.getY(), two.getX(), two.getY());
	}

	/**
	 * Calculates the euclidian distance between two points
	 * 
	 * @param x1
	 *            x coordinate of point one
	 * @param y1
	 *            y coordinate of point one
	 * @param x2
	 *            x coordinate of point two
	 * @param y2
	 *            y coordinate of point two
	 * @return Euclidian distance between two points
	 */
	public static final float distance(float x1, float y1, float x2, float y2) {

		final float dX = x1 - x2;
		final float dY = y1 - y2;
		return (float) Math.sqrt((dX * dX) + (dY * dY));
	}

	/**
	 * Calculates the euclidian distance between two points
	 * 
	 * @param one
	 *            Point one
	 * @param two
	 *            Point two
	 * @return Euclidian distance between two points
	 */
	public static final float distance(IPoint one, IPoint two) {
		return distance(one.getX(), one.getY(), two.getX(), two.getY());
	}

	/**
	 * @return the euclidean distance between the origin (0, 0) and (pX, pY).
	 */
	public static final float length(final float x, final float y) {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	/**
	 * @param min
	 *            min range inclusive min
	 * @param max
	 *            max range inclusive max
	 * @return random number
	 */
	public static final float random(final float min, final float max) {
		return min + RANDOM.nextFloat() * (max - min);
	}

	/**
	 * @param min
	 *            min range inclusive min
	 * @param max
	 *            max range inclusive max
	 * @return random number
	 */
	public static final int random(final int min, final int max) {
		return min + RANDOM.nextInt(max - min + 1);
	}

	/**
	 * @return Generates randomly 1 or -1
	 */
	public static final int randomSign() {
		return RANDOM.nextBoolean() ? 1 : -1;
	}

	/**
	 * Calculates the squared euclidian distance between two points
	 * 
	 * @param x1
	 *            x coordinate of point one
	 * @param y1
	 *            y coordinate of point one
	 * @param x2
	 *            x coordinate of point two
	 * @param y2
	 *            y coordinate of point two
	 * @return Squared Euclidian distance between two points
	 */
	public static final float squaredDistance(float x1, float y1, float x2, float y2) {
		final float dX = x1 - x2;
		final float dY = y1 - y2;
		return (dX * dX) + (dY * dY);
	}

	/**
	 * Calculates the squared euclidian distance between two points
	 * 
	 * @param one
	 *            Point one
	 * @param two
	 *            Point two
	 * @return Squared Euclidian distance between two points
	 */
	public static final float squaredDistance(IPoint one, IPoint two) {
		return squaredDistance(one.getX(), one.getY(), two.getX(), two.getY());
	}
}
