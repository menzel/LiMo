package lichen.controller;

import java.awt.Color;
import java.util.ArrayList; 

import javax.naming.NameNotFoundException; 

import lichen.model.Genus;
import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
import lichen.model.Species;

public class ColorMatcher {
	ArrayList<Measurement> mes; 

	public ColorMatcher() { 
		mes = MeasurementsFactory.getInstance().returnAll(); 

	}	


	/**
	 * Returns all Colors used on the image
	 * @return color array of all colors
	 */
	public Color[] getColors(){
		Color[] c = new Color[mes.size()];  
		//	Color[] c = new Color[1];  
		for(Measurement tmp:mes){
			tmp.getColor(); 
		} 
		return c;
	}


	/**
	 * Sets the user defined values of id to colors to the LichenMeasurements and to the Lichen
	 * @param id - id which was entered by the user 
	 * @param i - index of LichenMeasurements list
	 * @throws NameNotFoundException if id is not in the list
	 * @post Lichen owns an LichenMeasurements object and vice versa
	 */
	public void setColor(int id, int i) throws NameNotFoundException {
		try{ 
			mes.get(i).setSpecies(id);

		} catch (IndexOutOfBoundsException e){
			System.out.println("No measuremnt with this id found");
		}

		for(Measurement m: mes){
			if(m.getSpecies() == id){ 
				Species s = Genus.getInstance().getSpeciesFromID(id);
				
				//readd area if measurment with same species id was assigned before, color is lost
				if(s.hasMeasurement()){
					m.addArea(Math.round( s.getResults().getArea())); 
				}
				s.setResults(m);
			}
		}


	} 

} 
