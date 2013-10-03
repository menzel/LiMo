package lichen.main;

import ij.gui.Toolbar;
import lichen.controller.AutoAnalyzer;
import lichen.controller.DataImporter;
import lichen.controller.Processor;
import lichen.controller.UndoStack;
import lichen.view.FileHandler;
import lichen.view.MainGUI;

public class Main {


	/**
	 * Main Method of Flechtenanaylse, constructs classes and gives them to the GUI for further usage
	 * @param args
	 */ 
	public static void main(String[] args) {


		FileHandler fh = new FileHandler();
		AutoAnalyzer auto = new AutoAnalyzer();
		Processor myProcessor = new Processor();
		Toolbar t = new Toolbar();


		@SuppressWarnings("unused")
		DataImporter data = new DataImporter(); 

		fh.setLastDir(data.getInipath());

		@SuppressWarnings("unused")
		MainGUI gui = new MainGUI( fh, auto, myProcessor, t); 

	}

}
