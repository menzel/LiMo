package lichen.model;

import ij.process.ImageStatistics;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Represents the Results of measurements of one color 
 * 
 * @author menzel
 *
 */ 
public class Measurement implements Comparable<Measurement> {

	private double pixelarea;
	private Color color;
	private int species; 
	private ImageStatistics rawData;
	private double pixelrate = 1.0;
	private int thallusCount = 0;
	private ArrayList<String[]> thalliList = new ArrayList<String[]>();

	protected Measurement() {
	}

	/**
	 * Add results 
	 * @param imageStatistics - results to be added
	 */
	public void add(ImageStatistics imageStatistics) { 
		rawData = imageStatistics;
		if(rawData == null){
			System.err.println("No data object");
		}else{
			this.pixelarea = (long) rawData.area;
			//	System.out.println(rawData.area);
		}
	}


	/**
	 * @return color of this results
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * set color
	 * @param c - color
	 */
	public void setColor(Color c ) {
		this.color = c;

	}

	/**
	 * @return the species
	 */
	public int getSpecies() {
		return species;
	}

	/**
	 * @param species the species to set
	 */
	public void setSpecies(int species) {
		this.species = species;
	}

	/**
	 * @return the area in mm^2 using calibration
	 */
	public double getArea() {
		return this.pixelarea/this.pixelrate;
	}

	/**
	 * @param area the area to set in pixels
	 */
	public void setArea(double area) {
		this.pixelarea = area;
	}

	/**
	 * @param pixelrate the pixelrate to set
	 */
	public void setPixelrate(double pixelrate) {
		this.pixelrate = pixelrate;
	}

	/**
	 * Add area to pixelarea
	 * @param l - area to be added
	 */
	public void addArea(long l) {


		this.pixelarea += l; 
	}

	public double getPixelArea() {
		return this.pixelarea;
	}

	public int getCount(){
		return this.thallusCount;
	}

	public void setCount(int thallusCount) {
		this.thallusCount = thallusCount;

	}

	public void setThallusList(ArrayList<String[]> arrayList) {
		this.thalliList = arrayList; 

	}

	/**
	 * @return the thalliList
	 */
	public ArrayList<String[]> getThalliList() {
		return thalliList;
	}

	public double getPixelrate() {
		return this.pixelrate;
	}

	@Override
	public int compareTo(Measurement arg0) {
		if(this.species > arg0.getSpecies())
			return 0;
		else
			return -1; 
	} 

}
