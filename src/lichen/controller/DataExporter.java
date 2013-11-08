package lichen.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implements methods to export different Arrays to files
 */ 
public class DataExporter {

	/**
	 * Accepts an Object 2D Array and exports it to a given filename as comma seperated file
	 * seperator is stored in String comma
	 * file is created if not exists
	 * file is overwritten is exists
	 * @param Data - data to be exported
	 * @param path - file path and name
	 * .csv is added if not present
	 * @return true if export successful- false if not
	 * @post file 'path'.csv is created and data is written to it
	 */
	public boolean export(Object Data[][], String path){
		
		String comma=";";
		int i = 3;
		
		
		Pattern p = Pattern.compile(Pattern.quote(".csv"));
		Matcher pm = p.matcher(path);
		
		if(!pm.find()){
			path+=".csv"; 
		}

		try {
			FileWriter fstream = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(fstream);
			File file = new File(path);
			file.createNewFile(); 
			
			for(Object row[]: Data){
				for(Object value: row){
					if(value != null){ 
						out.write(value.toString()+comma); 
					}else{ 
						out.write(comma); 
					}
				}
				out.newLine(); 
				
				if(row[3] == null)
					i--;
				else
					i = 3;
				if(i == 0)
					break;
				
			} 
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 

		return true;

	}


}
