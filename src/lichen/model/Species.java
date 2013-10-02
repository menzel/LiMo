package lichen.model;

import java.awt.Color;

/**
 *Represents one species
 * @author menzel
 *
 */
public class Species {
	private String name;
	private int id;
	private Measurement results;

	private static int count = -1;
	private boolean s = false;
	private String genus;


	/**
	 * Constructor
	 * @param subClasses - list of species 
	 */
	public Species() {
		id = count+1;
		count++; 

	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Genus: " + name + ","
				+ (id != 0 ? id : "") + "]";
	}

	/**
	 * Set Name of Genus
	 * @param name - name to be set
	 */
	public void setName(String name){
		this.name  = name; 
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	public void setSu(boolean b) {
		this.s = b;

	}
	/**
	 * @return the s
	 */
	public boolean isS() {
		return s;
	}
	/**
	 * @return the results
	 */
	public Measurement getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(Measurement results) {
		this.results = results;
	}
	/**
	 * @return the genus
	 */
	public String getGenus() {
		return genus;
	}
	/**
	 * @param genus the genus to set
	 */
	public void setGenus(String genus) {
		this.genus = genus;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasMeasurement() {
		if(this.results != null){
			return true;

		}
		return false;
	}

}
