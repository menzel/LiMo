package lichen.main;

import ij.gui.Toolbar;
import lichen.controller.AutoAnalyzer;
import lichen.controller.ColorStack;
import lichen.controller.DataImporter;
import lichen.controller.Processor;
import lichen.controller.UndoStack;
import lichen.view.FileHandler;
import lichen.view.MainGUI;
/*

    This file is part of LiMo-Analyse.

    LiMo-Analyse is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    LiMo-Analyse is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with LiMo-Analyse.  If not, see <http://www.gnu.org/licenses/>.

 */
public class Main {


	/**
	 * Main Method of Flechtenanaylse, constructs classes and gives them to the GUI for further usage
	 * @param args
	 */ 
	public static void main(String[] args) {


		FileHandler fh = new FileHandler();
	//	AutoAnalyzer auto = new AutoAnalyzer();
		Processor myProcessor = new Processor();
		Toolbar t = new Toolbar();
		
		ColorStack.ColorStackInit();


		@SuppressWarnings("unused")
		DataImporter data = new DataImporter(); 

		fh.setLastDir(data.getInipath());

		@SuppressWarnings("unused")
		MainGUI gui = new MainGUI( fh, null, myProcessor, t); 

	}

}
