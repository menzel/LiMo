package de.thm.bi.utils;

import java.awt.Color;
import java.io.File;
import java.net.URISyntaxException;

import de.thm.bi.recognition.filter.PointFilterFactory.Filter;

/**
 * This Utility class stores Preferences of this application persistently.
 * 
 * @author Artur Klos
 * 
 */
public class Preferences {

	/**
	 * Preferences for Drawing
	 * 
	 * @author Artur Klos
	 * 
	 */
	public static final class Drawing {
		/**
		 * @return Returns the alpha value that should be used for cluster
		 *         drawing
		 */
		public static final float getDrawClusterAlphaValue() {
			return settings.getFloat(Prefs.DRAW_CLUSTER_ALPHA_VALUE, 1f);
		}

		/**
		 * @return Returns the color that is used to draw recognized pixels
		 */
		public static final Color getRecognizedPixelsDrawColor() {
			return new Color(settings.getInt(Prefs.RECOGNIZED_PIXELS_DRAW_COLOR, Color.BLACK.getRGB()));
		}

		/**
		 * @return Returns true if drawing cluster borders is enabled false
		 *         otherwise
		 */
		public static final boolean isDrawingClusterBorderEnabled() {
			return settings.getBoolean(Prefs.IS_DRAW_CLUSTER_BORDER_ENABLED, true);
		}

		/**
		 * @return Returns true if drawing the full clusters is enabled false
		 *         otherwise
		 */
		public static final boolean isDrawingClusterFullEnabled() {
			return settings.getBoolean(Prefs.IS_DRAW_CLUSTER_FULL_ENABLED, false);
		}

		/**
		 * @return Returns true if drawing recognized pixels is enabled false
		 *         otherwise
		 */
		public static final boolean isDrawingRecognizedPixelsEnabled() {
			return settings.getBoolean(Prefs.IS_DRAWING_RECOGNIZED_PIXELS_ENABLED, true);
		}

		/**
		 * Sets the alpha value that should be used for drawing clusters
		 * 
		 * @param value
		 */
		public static final void setDrawClusterAlphaValue(float value) {
			settings.setFloat(Prefs.DRAW_CLUSTER_ALPHA_VALUE, value);
		}

		/**
		 * En/Disables weather the cluster border should be drawn or not
		 * 
		 * @param value
		 */
		public static final void setDrawingClusterBorderEnabled(boolean value) {
			settings.setBoolean(Prefs.IS_DRAW_CLUSTER_BORDER_ENABLED, value);
		}

		/**
		 * En/Disables weather the full cluster should be drawn or not
		 * 
		 * @param value
		 */
		public static final void setDrawingClusterFullEnabled(boolean value) {
			settings.setBoolean(Prefs.IS_DRAW_CLUSTER_FULL_ENABLED, value);
		}

		/**
		 * Sets weather drawing recognized pixels is enabled
		 * 
		 * @param value
		 */
		public static final void setDrawingRecognizedPixelsEnabled(boolean value) {
			settings.setBoolean(Prefs.IS_DRAWING_RECOGNIZED_PIXELS_ENABLED, value);
		}

		/**
		 * Sets the color that is used to draw recognized pixels
		 * 
		 * @param color
		 */
		public static final void setRecognizedPixelsDrawColor(Color color) {
			settings.setInt(Prefs.RECOGNIZED_PIXELS_DRAW_COLOR, color.getRGB());
		}
	}

	private enum Prefs {
		DEFAULT_CORE_POINT_FILTER,
		DEFAULT_GRAY_SCALE,
		DONT_SHOW_IMAGEJ_WINDOWS,
		DRAW_CLUSTER_ALPHA_VALUE,
		IS_DRAW_CLUSTER_BORDER_ENABLED,
		IS_DRAW_CLUSTER_FULL_ENABLED,
		IS_DRAWING_RECOGNIZED_PIXELS_ENABLED,
		LAST_VIEWED_TOOLBAR,
		MAX_COLOR_DISTANCE,
		NUCLEI_COLOR_VECTOR_B,
		NUCLEI_COLOR_VECTOR_G,
		NUCLEI_COLOR_VECTOR_R,
		RECOGNIZED_PIXELS_DRAW_COLOR,
		DEFAULT_CLUSTER_SIZE
	}

	public static Vector3D normalizedNucleiColorVector;
	// RGB VALUES x>r y>g z>b
	public static Vector3D nucleiColorVector;

	public static Settings settings;

	/**
	 * @return Returns the current application path
	 */
	public static String getApplicationPath() {
		String path = "";

		try {
			path = Preferences.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		path = path.substring(0, path.lastIndexOf("/") + 1);
		if ((path.startsWith("/") || path.startsWith(File.separator))
				&& System.getProperty("os.name").toLowerCase().contains("win")) {
			path = path.substring(1);
			// TODO: AUF SEPERATOR ANPASSEN FALLS ES NICHT ORDENTLICH
			// FUNKTIONIERT
		}

		return path;
	}

	/**
	 * @return Returns the default Core Point Filter
	 */
	public static final Filter getDefaultCorePointFilter() {

		return Filter.valueOf(settings.getString(Prefs.DEFAULT_CORE_POINT_FILTER, Filter.HULL.toString()));
	}

	/**
	 * @return Returns the default grayscale
	 */
	public static final int getDefaultGrayscale() {
		return settings.getInt(Prefs.DEFAULT_GRAY_SCALE, 125);
	}


	/**
	 * @return Returns the maximum color distance that pc pixels may have to be
	 *         recognized as pc
	 */
	public static final float getMaxColorDistance() {
		return settings.getFloat(Prefs.MAX_COLOR_DISTANCE, 0.1f);
	}

	/**
	 * @return Gets the normalized pc colorvector
	 */
	public static final Vector3D getNormalizedNucleiColorVector() {
		return normalizedNucleiColorVector;
	}

	/**
	 * @return Gets the mean color of prostate cancer as a vector
	 */
	public static final Vector3D getNucleiColorVector() {
		return nucleiColorVector;
	}

	/**
	 * Initializes all preferences. This should be called before using any
	 * preference
	 */
	public static final void init() {
		settings = new Settings();

		float r = settings.getFloat(Prefs.NUCLEI_COLOR_VECTOR_R, 110);
		float g = settings.getFloat(Prefs.NUCLEI_COLOR_VECTOR_G, 45);
		float b = settings.getFloat(Prefs.NUCLEI_COLOR_VECTOR_B, 125);
		nucleiColorVector = new Vector3D(r, g, b);
		normalizedNucleiColorVector = nucleiColorVector.copy();
		normalizedNucleiColorVector.normalize();
	}

	/**
	 * @return Returns true if imagej window closing is enabled else falsecla
	 */
	public static final boolean isDontShowImageJWindowsEnabled() {
		return settings.getBoolean(Prefs.DONT_SHOW_IMAGEJ_WINDOWS, true);
	}

	/**
	 * @param filter
	 *            Sets the default Core Point Filter
	 */
	public static final void setDefaultCorePointFilter(Filter filter) {
		settings.setString(Prefs.DEFAULT_CORE_POINT_FILTER, filter.toString());
	}

	/**
	 * Sets the default grayscale
	 * 
	 * @param value
	 * 
	 */
	public static final void setDefaultGrayscale(int value) {
		settings.setInt(Prefs.DEFAULT_GRAY_SCALE, value);
	}

	/**
	 * Sets weather the imagej windows should be closed or not
	 * 
	 * @param value
	 */
	public static final void setDontShowImageJWindow(boolean value) {
		settings.setBoolean(Prefs.DONT_SHOW_IMAGEJ_WINDOWS, value);
	}


	/**
	 * Sets the maxiumum color distance that pc pixels may have to be recognized
	 * as pc
	 * 
	 * @param distance
	 * 
	 */
	public static final void setMaxColorDistance(float distance) {
		settings.setFloat(Prefs.MAX_COLOR_DISTANCE, distance);
	}

	/**
	 * @return Returns the default cluster Size
	 */
	public static final int getDefaultClusterSize() {
		return settings.getInt(Prefs.DEFAULT_CLUSTER_SIZE, 50);
	}

	/**
	 * Sets the default cluster size
	 * 
	 * @param value
	 */
	public static final void setDefaultClusterSize(int value) {
		settings.setInt(Prefs.DEFAULT_CLUSTER_SIZE, value);
	}

	/**
	 * Sets the mean color vector of nuclei
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public static final void setNucleiColorVector(float r, float g, float b) {
		nucleiColorVector.x = r;
		nucleiColorVector.y = g;
		nucleiColorVector.z = b;
		settings.setFloat(Prefs.NUCLEI_COLOR_VECTOR_R, r);
		settings.setFloat(Prefs.NUCLEI_COLOR_VECTOR_R, g);
		settings.setFloat(Prefs.NUCLEI_COLOR_VECTOR_R, b);
		normalizedNucleiColorVector = nucleiColorVector.copy();
		normalizedNucleiColorVector.normalize();
	}

}
