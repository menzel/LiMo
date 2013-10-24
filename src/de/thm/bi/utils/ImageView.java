package de.thm.bi.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

/**
 * ImageView class that is responsible for drawing Images. It is able to hold a
 * OnPaintListener that is called on every repainting of the ImageView
 * 
 * @author Artur Klos
 * 
 */
public class ImageView extends JComponent {

	private static final long serialVersionUID = -1188286807111011291L;
	private Image img;
	private OnPaintListener onPaint;
	private int w, h;

	/**
	 * Creates a new ImageView object
	 * 
	 * @param img
	 *            Image that will be drawn by this view
	 */
	public ImageView(Image img) {
		this.img = img;
		w = img.getWidth(null);
		h = img.getHeight(null);
		setPreferredSize(new Dimension(w, h));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, w, h, null);
		if (onPaint != null) {
			onPaint.paintComponent(g);
		}
	}

	/**
	 * Sets the OnPaintListener
	 * 
	 * @param onPaint
	 *            The Listener that will be called on paint
	 */
	public void setOnPaint(OnPaintListener onPaint) {
		this.onPaint = onPaint;
	}

}
