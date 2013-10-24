package lichen.model;

import java.util.ArrayList;

import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;


/**
 * Factory for Measurement, holds references to all objects
 * @author menzel
 *
 */
public class MeasurementsFactory {
	private ArrayList<Measurement> measurements = new ArrayList<Measurement>();
	private static MeasurementsFactory instance;
	private double pixelrate = 1.0; 
	public static double tmpArea =0;

	public static MeasurementsFactory getInstance(){
		if(instance  == null){
			instance = new MeasurementsFactory();
		} 
		return instance; 

	}


	/**
	 * creates a new measurement and adds it to the list
	 * @return the new measurement 
	 */
	public Measurement createMeasurement(){
		Measurement measurement = new Measurement();
		measurement.setPixelrate(this.pixelrate);

		measurements.add(measurement);
		return measurement; 
	}


	/**
	 * Getter for all Measurement objects
	 * @return all measurements 
	 */
	public ArrayList<Measurement> returnAll() {
		return this.measurements;
	}

	/**
	 * Returns the Area of all lichen
	 * @return - area 
	 */
	public double returnAllArea(){
		double area =0;
		for(Measurement measurement: measurements){

			area+=measurement.getArea(); 
		}

		return area;

	}
	/**
	 * @param pixelrate the pixelrate to set
	 */
	public void setPixelrate(double pixelrate) {
		this.pixelrate = pixelrate;
	}


	
	/**
	 * Deletes all Measurements
	 * @post: measurements list is empty
	 */
	public void reset() {
		this.measurements.clear(); 
	}


	/**
	 * Returns measurment by a given ID
	 * @param id - id of the measurement 
	 * @return - measurment which has the given id, null if not exists
	 * @pre - ids must be set, do not use in AutoAnalyer context
	 */
	public Measurement getMeasurementByID(int id) {
		for(Measurement tmp: this.measurements){
			if(tmp.getSpecies() == id){
				return tmp;
			} 
		}
		return null;
	}
	
	/**
	 * Getter Pixelreate
	 * @return this pixelrate
	 */
	public double getPixelrate(){ 
		return this.pixelrate;
	} 
}
