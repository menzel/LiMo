package de.thm.bi.utils;

import java.awt.Color;
import java.util.LinkedList;

import de.thm.bi.recognition.data.IPoint;

/**
 * Vector pool that allows to recycle Vectors
 * 
 * @author Artur Klos
 * 
 */
public class Vector3DPool {
	private static final LinkedList<Vector3D> pool = new LinkedList<Vector3D>();

	/**
	 * 
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return Returns a Vector with the provided coordinates
	 */
	public static final Vector3D obtain(float x, float y, float z) {
		Vector3D vec = pool.isEmpty() ? new Vector3D() : pool.remove();
		vec.setXYZ(x, y, z);
		return vec;
	}

	public static final Vector3D obtain(IPoint point) {
		Color c = point.getColor();
		return obtain(c.getRed(), c.getGreen(), c.getBlue());
	}

	/**
	 * Recycles a Vector
	 * 
	 * @param vec
	 *            Vector that will be recycled
	 */
	public static final void recycle(Vector3D vec) {
		pool.add(vec);
	}
}
