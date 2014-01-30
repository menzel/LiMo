package lichen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lichen.model.MeasurementsFactory;

/**
 * Assign area to lichen depeding on the selected row 
 * if a blank row is selected a new measurement is made
 */ 
class AssignActionListener implements ActionListener {
	
	private static AssignActionListener instance;
	private MainGUI gui;
	
	
	private AssignActionListener() {
	}

	public static AssignActionListener getInstace(){
		if(instance == null)
			instance = new AssignActionListener();
		return instance;	
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		gui = MainGUI.getInstance();
		MeasurementsFactory factory = MeasurementsFactory.getInstance();
		int speciesID;


		if(factory.returnAll().size() <= gui.measurements.getSelectedRow()){
			try{ 
				speciesID = Integer.parseInt(gui.id.getText()); 
				if(gui.manualAnalyzer.assign(speciesID)){ 
					gui.text.setText(gui.text.getText() + "\n" + "Fläche erfolgreich zugewiesen"); 
					gui.id.setText("");	

					if(gui.measurements.getSelectedRow() < gui.measurements.getRowCount()-1 ){ 
						gui.measurements.setRowSelectionInterval(gui.measurements.getSelectedRow()+1, gui.measurements.getSelectedRow()+1);//move selection one row down
					}else{ 
						gui.text.setText(gui.text.getText() + "\n" + "Nur 10 Einträge möglich");
					}

				}else{ 
					gui.text.setText(gui.text.getText() + "\n" + "Es existiert kein Eintrag mit dieser ID, die ID besitzt bereits eine Farbe oder die gemessene Fläche ist 0"); 
				} 

			} catch(NumberFormatException e){ 
				e.printStackTrace();
				gui.text.setText(gui.text.getText() + "\n" + "Es wurde keine Nummer eingegeben");
			} 
		}else{ // re-adding area to existing measurement 

			Object o = gui.measurements.getValueAt(gui.measurements.getSelectedRow(), 0); 
			
			int idForChangeId;
			try{ 
				idForChangeId = Integer.parseInt(gui.id.getText());
			}catch(NumberFormatException e){ 
				//readd instead of chang id
				idForChangeId = Integer.MAX_VALUE; 
			}

			if(gui.manualAnalyzer.readd(o, idForChangeId )){ 

				gui.text.setText(gui.text.getText() + "\n" + "Fläche erfolgreich zugewiesen"); 
				gui.id.setText("");
			}else{ 
				gui.text.setText(gui.text.getText() + "\n" + "Zuweisung nicht erfolgreich, ID nicht gefunden"); 
			}
		}

		int index =0;

		//set overview table
		Object[][] measurementValues = gui.getMeasurements();
		for(Object[] row: measurementValues){
			if(row[0] == null)
				return;
			int columnCount =0;
			for(Object o: row){ 
				gui.measurements.setValueAt(o, index, columnCount++);
			} 
			index++;
		}
		//set table

		gui.setNewColor(true);


	} 
} 
