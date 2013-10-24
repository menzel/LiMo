package de.thm.bi.recognition.data;

import java.awt.Color;

import de.thm.bi.utils.AttributeStore;
import de.thm.bi.utils.ID;
import de.thm.bi.utils.IDFactory;
import de.thm.bi.utils.Vector3D;

/**
 * Implementation of the IPoint interface
 * 
 * @author Artur Klos
 */
public class Point implements IPoint {

	private AttributeStore attributeStore = new AttributeStore();
	private Classification classification = Classification.UNCLASSIFIED;
	private Color color;
	private Vector3D colorVector;
	private ID id = IDFactory.createApplicationUniqueId();
	public final int x;
	public final int y;

	public Point() {
		this(0, 0);
	}

	public Point(Classification classification, int x, int y) {
		this(Classification.UNCLASSIFIED, x, y, new Color(0, 0, 0, 0));
	}

	public Point(Classification classification, int x, int y, Color color) {
		this.classification = classification;
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public Point(int x, int y) {
		this(Classification.UNCLASSIFIED, x, y);
	}

	@Override
	public int compareTo(IPoint o) {
		if (x == o.getX())
			return y - o.getY();
		else
			return x - o.getX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.IPoint#cross(dbpcanalyzer.ak.clustering
	 * .data.IPoint)
	 */
	public int cross(IPoint p) {
		return x * p.getY() - y * p.getX();
	}

	@Override
	public Classification getClassification() {
		return classification;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Vector3D getColorVector() {
		if (colorVector == null) {
			colorVector = new Vector3D(color.getRed(), color.getGreen(), color.getBlue());
		}
		return colorVector.copy();
	}

	@Override
	public long getId() {
		return id.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#getStoredAttribute(java.lang.Object
	 * )
	 */
	@Override
	public Object getStoredAttribute(Object attributeName) {
		return attributeStore.getStoredAttribute(attributeName);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#hasStoredAttribute(java.lang.Object
	 * )
	 */
	@Override
	public boolean hasStoredAttribute(Object attributeName) {
		return attributeStore.hasStoredAttribute(attributeName);
	}

	@Override
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
		colorVector = new Vector3D(color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void setColorVector(Vector3D colorVec) {
		this.colorVector = colorVec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#storeAttribute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void storeAttribute(Object attributeName, Object object) {
		attributeStore.storeAttribute(attributeName, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.clustering.data.IPoint#sub(dbpcanalyzer.ak.clustering
	 * .data.IPoint)
	 */
	public IPoint sub(IPoint p) {
		return new Point(x - p.getX(), y - p.getY());
	}

}
