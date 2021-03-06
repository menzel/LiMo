package lichen.controller;

import ij.ImagePlus;
import ij.gui.Toolbar;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.naming.NameNotFoundException;
import javax.swing.JOptionPane;

import lichen.model.Genus;
import lichen.model.Measurement;
import lichen.model.MeasurementsFactory;
import lichen.model.Species;
import lichen.view.MainGUI;

public class ManualAnalyzer {

	private FloodFiller floodfiller; 
	private MainGUI gui;
	private ImagePlus imp;
	private double MaxArea;
	private int blurValue = 1;
	private MeasurementsFactory factory;
	private static boolean active = false;

	/**
	 * Constructor
	 * @pre mainGui has to exist
	 * @param imp - image which is to be analyzed
	 */
	public ManualAnalyzer(ImagePlus imp) {
		this.imp = imp;	
		this.gui = MainGUI.getInstance();
		active = true;

		ColorStack.resetStack();

		gui.getMyProcessor().background(imp,blurValue); 
		imp.getProcessor().setValue(ColorStack.pop().getRGB());


		//orig. flood filler:
		floodfiller = new FloodFiller(imp.getProcessor()); 
		floodfiller.setLinewidth(gui.getBorderWidth());
		//end

		gui.getIc().addMouseListener(new manualMouseListener());
		MaxArea = imp.getWidth()*imp.getHeight()*(20.0/100); //set default 20.0% maxArea;

		factory = MeasurementsFactory.getInstance();
		double rate  = gui.getMyProcessor().getPixelRate(imp); 
		factory.setPixelrate(rate); 

		floodfiller.setPixelrate(Math.sqrt(rate));
		gui.setPixelrate(Math.sqrt(rate));

	}

	/**
	 * Undo a floodfill and areaAdd
	 * @return true if undo was done, false if undo could not be done (when old point is not 
	 * set or the last AreaAdd was an readd to an existing measurement
	 */
	public boolean undo(){	

		boolean done = floodfiller.unfill(); 

		imp.updateAndDraw();
		return done;

	}

	/**
	 * Assign a measured area to a lichen and vice versa
	 * @param speciesID - id of the species
	 * @return true if SUCCESSFUL, false if ID was not found 
	 */
	public boolean assign(int speciesID){


		Genus genus = Genus.getInstance();
		try{


			if(genus.exists(speciesID)){
				if(genus.getSpeciesFromID(speciesID).getResults() == null){
					if( floodfiller.getPixelCount() != 0){ 

						Measurement manualMeasured = factory.createMeasurement();

						manualMeasured.setArea(floodfiller.getPixelCount());
						manualMeasured.setCount(floodfiller.getThallusCount());
						manualMeasured.setColor(gui.convertFromIJIntToColor(imp.getProcessor().getValue()));
						manualMeasured.setThallusList(floodfiller.getThalliList());

						genus.getSpeciesFromID(speciesID).setResults(manualMeasured);
						manualMeasured.setSpecies(speciesID); 				

						floodfiller.setPixelCount(0); 
						floodfiller.setThallusCount(0);
						floodfiller.setThalliList(new ArrayList<String[]>());

						imp.getProcessor().setValue(ColorStack.pop().getRGB());  

						return true;
					}else{

						return false; 
					}
				} else{ 
					// not allowed
					return false;
				}
			}
		}catch (NameNotFoundException e){
			return false;
		}
		return false;

	}

	/**
	 * MouseListener, triggers when user clicks on image
	 */
	private class manualMouseListener implements MouseListener {

		@Override
		public void mouseReleased(MouseEvent e) { 
		}

		@Override
		public void mousePressed(MouseEvent e) { 
		}

		@Override
		public void mouseExited(MouseEvent e) { 
		}

		@Override
		public void mouseEntered(MouseEvent e) { 
		}

		@Override
		public void mouseClicked(MouseEvent e) { 

			switch (Toolbar.getToolId()) {
			case 7: 
				addArea();
				break;

			default:
				break;
			}

		}
	}
	/**
	 * Setter Max
	 * @param d - value to be set
	 */
	public void setMax(double d) {
		this.MaxArea = d; 

	}


	/**
	 * Add to Area current flood filler area
	 * reads selected pixels, does a flood fill and adds to the measurement
	 * @pre Roi on picture has to be defined
	 * @post Roi and area around it is filled with background color and pixel countet
	 */
	public void addArea() {
		long last = floodfiller.getPixelCount(); 
		int option = 0; 
		int x = imp.getRoi().getPosX();
		int y = imp.getRoi().getPosY();
		long oldPixelcount = floodfiller.getPixelCount();
		//	int oldColor = 0;

		if(!(factory.returnAll().size() <= gui.measurements.getSelectedRow())){ 
			if(floodfiller.getPixelCount() == 0){ 

				Object o = gui.measurements.getValueAt(gui.measurements.getSelectedRow(), 0); 
				Measurement tmp = factory.getMeasurementByID(Integer.parseInt(o.toString()));

				//			oldColor = imp.getProcessor().getValue();
				imp.getProcessor().setValue(tmp.getColor().getRGB());
			}
		} 

		if(!floodfiller.fill(imp.getRoi().getPosX(), imp.getRoi().getPosY())){
			JOptionPane.showMessageDialog(null, "Ein Thallus kann nicht doppelt gezählt werden");
		}

		imp.updateAndDraw();
		gui.getText().setText(gui.getText().getText() + "\nHinzugefügt: " + x + ":" + y + " Fläche: " + (Math.round((floodfiller.getPixelCount()-oldPixelcount)/MeasurementsFactory.getInstance().getPixelrate()*100.0))/100.0);

		if(MaxArea < floodfiller.getPixelCount()-last){
			option = JOptionPane.showConfirmDialog(null, "Die hinzugefüge Fläche umfasst einen Großteil des Messbereichts\n" +
					"Ist die Auswahl richtig?"); 
		} 

		if(option == JOptionPane.NO_OPTION){
			undo();
			imp.updateAndDraw();
		}	

	}


	/**
	 * Calculates the current area in mm^2 from the pixelvalues 
	 * @return
	 */
	public double getArea() {
		return gui.getMyProcessor().convertFromPixel(floodfiller.getPixelCount() ,imp);
	}


	/**
	 * Adds an area to an existing measurement or is used to change the id of a measurement
	 * @param o - entry of the existing measurement
	 * @param speciesID for changing id in measurement 
	 * @return true if it worked, false if name not found
	 */
	public boolean readd(Object o, int speciesID) {

		//readd:
		if(floodfiller.getPixelCount() != 0){
			Measurement tmp = factory.getMeasurementByID(Integer.parseInt(o.toString()));
			tmp.addArea(floodfiller.getPixelCount());
			tmp.getThalliList().addAll(floodfiller.getThalliList()); 
			floodfiller.setThalliList(new ArrayList<String[]>());

			//TODO: do not pop new color, just restore the one before readding
			imp.getProcessor().setValue(ColorStack.pop().getRGB());


			floodfiller.setPixelCount(0); 
			return true;
		}else{
			//change ID:

			Genus genus = Genus.getInstance();
			Object tableID = gui.measurements.getValueAt(gui.measurements.getSelectedRow(), 0);

			int oldID = Integer.parseInt(tableID.toString());

			Species tmpSpe;
			try {
				tmpSpe = genus.getSpeciesFromID(oldID);
				Measurement tmpMes = tmpSpe.getResults();

				genus.getSpeciesFromID(speciesID).setResults(tmpMes);
				tmpMes.setSpecies(speciesID);

				tmpSpe.setResults(null);
			} catch (NameNotFoundException e) {
				return false;
			}

			return true;
		}
	}


	public static boolean getActive(){
		return active;
	} 

}
