package lichen.main;

import ij.ImagePlus;

import java.awt.Color;

import lichen.controller.AutoAnalyzer;
import lichen.controller.DataImporter;
import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
import lichen.view.FileHandler;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) { 
	

	//	createMeasurements();
		gui();

		//	test();
	}

	private static void createMeasurements() {
		Color[] colors = new Color[10];
		

		colors[0] = Color.red;
		colors[1] = Color.blue;
		colors[2] = Color.yellow;
		colors[3] = Color.cyan;
		colors[4] = Color.magenta;

		MeasurementsFactory mfactory = MeasurementsFactory.getInstance();
		for(int i = 0; i<5;i++){ 

			Measurement lichen = mfactory.createMeasurement();
			lichen.setColor(colors[i]);
			lichen.setArea((long)Math.pow(i, 1.5)+i*10+i);
 
		} 
	}

	public static void gui(){
		DataImporter data = new DataImporter(); 
//		MainGUI gui = new MainGUI();
	}
	public static void test(){
		//		Toolbar t = new Toolbar(); 
		//		Toolbar.installStartupMacrosTools();
		//		Toolbar.setBrushSize(1); 
		//		Toolbar.setForegroundColor(new Color(255));
		//		t.installBuiltinTool("Brush");

		//		t.setTool(13); // 11-zoom.
		//		t.setVisible(true);

		FileHandler fileHandler = new FileHandler();
		ImagePlus imp = fileHandler.openImagePlus();
		//
		AutoAnalyzer auto = new AutoAnalyzer();

		imp.show();
	//	Processor p =  new Processor();
	//	p.substrackBackground(imp, 20); 
		imp.show();

		auto.autoAnalyze(imp);
		//DataImporter data = new DataImporter(); 
	}
}
