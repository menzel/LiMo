package lichen.fileHandling;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import lichen.controller.UndoStack;
import lichen.model.Genus;
import lichen.model.Species;
import lichen.view.MainGUI;

public class INIfileReader {

	private Genus lichen;
	private String inipath = "";
	private ArrayList<Pattern> patternList = new ArrayList<Pattern>();

	
	/**
	 * creates pattern for INI File reading 
	 */
	public INIfileReader() {

		patternList.add(Pattern.compile(Pattern.quote("[")));
		patternList.add(Pattern.compile(Pattern.quote("Anzahl=")));
		patternList.add(Pattern.compile(Pattern.quote("inipath=")));
		patternList.add(Pattern.compile(Pattern.quote("style=")));
		patternList.add(Pattern.compile(Pattern.quote("history=no")));
		
		
		openStream();
		//	test();		
	}


	/**
	 * Opens the INI file for parsing lichen names and species
	 * if FLECHTE.INI is not in the program directory a file chooser dialog is displayed
	 * calls the parsing method when the file was found
	 */
	private void openStream(){

		FileInputStream stream = null;
		try {
			stream = new FileInputStream("ARTENLISTE.INI");

		} catch (FileNotFoundException e) { 
			JFileChooser filechooser = new JFileChooser();
			filechooser.showDialog(null, "Wähle .ini Datei");
			String path = filechooser.getSelectedFile().toString();
			try {
				stream = new FileInputStream(path);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Keine INI Datei gefunden");
			}

		}
		try{ 
			readLichenINIFile(stream);
		}catch (Exception e){
			JOptionPane.showMessageDialog(null, "Fehler in der ARTENLISTE.INI Datei:\n\n" +  e.getMessage() + "\nLiMo-Anyalse kann nicht gestartet werden.\nBitte editieren sie die FLECHTE.INI.\n",  "Fehler INI Datei" , JOptionPane.ERROR_MESSAGE );
		}

	}


	/**
	 * Reads the INI File with the Name FLECHTE.INI
	 * creates species objects from INI FIle 
	 * @pre: FLECHTE.INI must be in programm directory 
	 * @post: species are species-object in lichen 
	 * @post lichen.genusCount is set
	 * @post if longHistory is disabled: set Flag method is called in UndoStack
	 * @post if modern style is enabled: flag is set in MainGui
	 * @throws IOException if read failed
	 */
	private void readLichenINIFile(FileInputStream stream) throws Exception{
		lichen = Genus.getInstance();
		
		try {
			DataInputStream in = new DataInputStream(stream);

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String s;
			

			
			boolean flag = false;
			Species species;
			int i = 0;
			int geni =0;

			species = new Species();
			String genus = "none";
			String lastGenus = ""; 

			while((s = reader.readLine()) != null){
				Matcher gm = patternList.get(0).matcher(s);
				Matcher nm = patternList.get(1).matcher(s);
				Matcher pm = patternList.get(2).matcher(s); 
				Matcher styleM = patternList.get(3).matcher(s);
				Matcher memoryM = patternList.get(4).matcher(s);
				
				if(pm.find()){
					this.inipath = s.substring(9,s.length()-1); 
				}
				
				if(memoryM.find()){
					UndoStack.setLongHistory(false);
				}
				
				if(styleM.find()){
					if(s.substring(6, s.length()).equalsIgnoreCase( "modern")){
						MainGUI.setStyleModern(true);
					}
				}
			
				if(gm.find()){
					flag = true; 
					if(!lastGenus.equals(genus)){
						lastGenus = genus;
						geni++;
					}
					genus = s.substring(1,s.length()-1);


				}else{ 

					if(nm.find() ){
						i = Character.getNumericValue(s.charAt(7));
						if(s.length() > 8){ 

							i = i*10 + Character.getNumericValue(s.charAt(8));							
						}

					} else{ 
						if(i != 0 && !(nm.find()) ){
							species = new Species(); 

							if(Character.isLetter(s.charAt(2))){ 
								species.setName(s.substring(2)); 
							}else{ 
								species.setName(s.substring(3)); 
							}
							species.setGenus(genus);
							this.lichen.push(species); 
							i--;
						}

						if(flag && i ==0){
							flag = false;
						}
					}
				}
			}
			this.lichen.setGenusCount(geni);
			in.close();

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the inipath
	 */
	public String getInipath() {
		return inipath;
	}



}
