package de.thm.bi.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.normalize.INormalization.Attributes;

/**
 * A bunch of utils for drawing images
 * 
 * @author Artur Klos
 * 
 */
public class ImageUtils {

	public static final void writeFile(String path, IDataSet dataSet) {
		try {
			// retrieve image
			BufferedImage image = new BufferedImage((int) dataSet.getStoredAttribute(Attributes.IMAGE_WIDTH),
					(int) dataSet.getStoredAttribute(Attributes.IMAGE_HEIGHT), BufferedImage.TYPE_INT_ARGB);
			drawClustersWithRandomColor(image, dataSet.getClusters());
			File outputfile = new File(path);
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears the whole image
	 * 
	 * @param img
	 *            Image to clear
	 */
	public static final void clear(BufferedImage img) {
		BufferedImage clearImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		img.setData(clearImg.getRaster());
	}

	/**
	 * Draws a Cluster
	 * 
	 * @param img
	 *            Img to draw on
	 * @param cluster
	 *            Cluster to draw
	 */
	public static final void drawCluster(BufferedImage img, IRegion cluster) {
		List<IPoint> points = cluster.getAllPoints();
		Color color = getRandomColor();
		drawList(img, points, color);

	}

	/**
	 * Draws a cluster with a random color
	 * 
	 * @param graphics
	 *            Graphics to draw on
	 * @param cluster
	 *            Cluster to draw
	 */
	public static final void drawCluster(Graphics2D graphics, IRegion cluster) {
		List<IPoint> points = cluster.getAllPoints();
		graphics.setColor(getRandomColor());
		drawList(graphics, points);
	}

	/**
	 * Draws the border of a Cluster
	 * 
	 * @param img
	 *            Img to draw on
	 * @param cluster
	 *            Cluster to draw
	 */
	public static final void drawClusterBorder(BufferedImage img, IRegion cluster) {
		Color color = getRandomColor();
		drawList(img, cluster.getFullHull(), color);

	}

	/**
	 * Draws the border of the cluster with a random color
	 * 
	 * @param graphics
	 *            Graphics to draw on
	 * @param cluster
	 *            Cluster to draw
	 */
	public static final void drawClusterBorder(Graphics2D graphics, IRegion cluster) {
		List<IPoint> points = cluster.getFullHull();
		graphics.setColor(getRandomColor());
		drawList(graphics, points);
	}

	/**
	 * Draws a list of clusters with a random color
	 * 
	 * @param img
	 *            Image that will be drawn on
	 * @param clusters
	 *            Clusters that will be drawn
	 */
	public static final void drawClustersWithRandomColor(BufferedImage img, List<IRegion> clusters) {
		int limit = clusters.size();
		for (int i = 0; i < limit; i++) {
			drawCluster(img, clusters.get(i));
		}
	}

	/**
	 * Draws a List of Clusters with a random color
	 * 
	 * @param graphics
	 *            Graphics to draw om
	 * @param clusters
	 *            Clusters to draw
	 */
	public static final void drawClustersWithRandomColor(Graphics2D graphics, List<IRegion> clusters) {
		int limit = clusters.size();
		for (int i = 0; i < limit; i++) {
			drawCluster(graphics, clusters.get(i));
		}
	}

	/**
	 * Draws a the border of a list of Clusters with a random color
	 * 
	 * @param img
	 *            Img to draw on
	 * @param clusters
	 *            Clusters to draw
	 */
	public static final void drawClustersWithRandomColorBorderOnly(BufferedImage img, List<IRegion> clusters) {
		int limit = clusters.size();
		for (int i = 0; i < limit; i++) {
			drawClusterBorder(img, clusters.get(i));
		}
	}

	/**
	 * Draws the border of a clusterlist with a random color for each Cluster
	 * 
	 * @param graphics
	 *            Graphics to draw om
	 * @param clusters
	 *            Clusters to draw
	 */
	public static final void drawClustersWithRandomColorBorderOnly(Graphics2D graphics, List<IRegion> clusters) {
		int limit = clusters.size();
		for (int i = 0; i < limit; i++) {
			drawClusterBorder(graphics, clusters.get(i));
		}
	}

	/**
	 * Draws a list of points with a specified color
	 * 
	 * @param img
	 *            Image to draw on
	 * @param points
	 *            Points to draw
	 * @param drawingColor
	 *            Drawing color to use
	 */
	public static final void drawList(BufferedImage img, List<IPoint> points, Color drawingColor) {
		int limit = points.size();
		int rgb = drawingColor.getRGB();
		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			int x = point.getX(), y = point.getY();
			img.setRGB(x, y, rgb);
		}
	}

	/**
	 * Draws a list of points.
	 * 
	 * @param ig2
	 *            Graphics to draw on
	 * @param points
	 *            Points list to draw
	 */
	public static final void drawList(Graphics2D ig2, List<IPoint> points) {
		int limit = points.size();
		for (int i = 0; i < limit; i++) {
			IPoint point = points.get(i);
			ig2.drawRect((int) point.getX(), (int) point.getY(), 1, 1);
		}
	}

	/**
	 * Draws a list of points with the specified color
	 * 
	 * @param ig2
	 *            Graphics to draw on
	 * @param points
	 *            Points to draw
	 * @param color
	 *            Color that will be drawn with
	 */
	public static final void drawList(Graphics2D ig2, List<IPoint> points, Color color) {
		ig2.setColor(color);
		drawList(ig2, points);
	}

	/**
	 * @return Returns a random color
	 */
	private static final Color getRandomColor() {
		float r = MathUtils.random(0, 0.98f);
		float g = MathUtils.random(0, 0.98f);
		float b = MathUtils.random(0, 0.98f);
		float a = Preferences.Drawing.getDrawClusterAlphaValue();
		if (a < 0 || a > 1) {
			System.out.println(a);
		}
		if (r < 0 || r > 1) {
			System.out.println(a);
		}
		if (g < 0 || g > 1) {
			System.out.println(a);
		}
		if (b < 0 || b > 1) {
			System.out.println(a);
		}

		try {
			return new Color(r, g, b, a);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
