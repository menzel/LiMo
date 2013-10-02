package de.thm.bi.recognition.filter;

import de.thm.bi.utils.Preferences;

/**
 * This class provides functions for creating IPointFilter<br>
 * 
 * @author Artur Klos
 * 
 */
public class PointFilterFactory {

	public enum Filter {
		COLOR_ANGLE, CONVEX_HULL, CURVATURE, GRAYSCALE, HULL, LINE_BETWEEN_POINTS
	}

	public static final IPointFilter createDefault() {
		return createFilter(Preferences.getDefaultCorePointFilter());
	}

	public static final IPointFilter createFilter(Filter criteria) {

		switch (criteria) {
		case COLOR_ANGLE:
			return new ColorAngle();
		case HULL:
			return new Hull();
		case CURVATURE:
			return new Curvature();
		case LINE_BETWEEN_POINTS:
			return new LBCHPoints();
		case CONVEX_HULL:
			return new ConvexHull();
		case GRAYSCALE:
			return new Grayscale();
		}
		return null;
	}
}
