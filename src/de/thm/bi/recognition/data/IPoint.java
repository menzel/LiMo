package de.thm.bi.recognition.data;

import java.awt.Color;

import de.thm.bi.utils.IAttributeStore;
import de.thm.bi.utils.Vector3D;

/**
 * This interface describes methods for points
 * 
 * @author Artur Klos
 * 
 */
public interface IPoint extends Comparable<IPoint>, IAttributeStore {

	/**
	 * Classifications of Points
	 */
	public enum Classification {
		CLASSIFIED, NOISE, UNCLASSIFIED
	}

	/**
	 * @param p
	 *            Second point/vector
	 * @return cross product of two vectors
	 */
	public int cross(IPoint p);

	/**
	 * Gets the Classification of this Point
	 * 
	 * @return Returns the Classification
	 */
	public Classification getClassification();

	/**
	 * @return Returns the Color of this point
	 */
	public Color getColor();

	/**
	 * @return Returns the Color Vector of this point
	 */
	public Vector3D getColorVector();

	/**
	 * Gets the Id of this Point it has to be unique
	 * 
	 * @return Returns the Id of this object
	 */
	public long getId();

	/**
	 * @return X Position of this Point
	 */
	public int getX();

	/**
	 * @return Y Position of this Point
	 */
	public int getY();

	/**
	 * Sets the Classification of this Point
	 * 
	 * @param classification
	 *            Classification to set
	 */
	public void setClassification(Classification classification);

	/**
	 * @return Sets the color of this point
	 */
	public void setColor(Color color);

	/**
	 * Sets the ColorVector of this point
	 * 
	 * @param colorVec
	 *            ColorVector of this point
	 */
	public void setColorVector(Vector3D colorVec);

	/**
	 * @param p
	 *            Point/Vector to subtract
	 * @return subtraction of two points
	 */
	public IPoint sub(IPoint p);
}
