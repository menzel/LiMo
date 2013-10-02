package de.thm.bi.utils;

import java.awt.Graphics;

/**
 * Listener for reacting on paint Events
 * 
 * @author Artur Klos
 * 
 */
public interface OnPaintListener {
	/**
	 * Reaction for on paint events
	 * 
	 * @param g
	 *            Graphics that can be drawn on
	 */
	void paintComponent(Graphics g);
}
