package lichen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import lichen.controller.DataExporter;
import lichen.model.Genus;

/**
 * ActionListern for saving data to file
 * @author menzel
 *
 */
final class BsaveActionListener implements ActionListener {
	/**
	 * 
	 */
	private MainGUI gui;

	/**
	 * @param mainGUI
	 */
	BsaveActionListener(MainGUI mainGUI) {
		gui = mainGUI;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		DataExporter d = new DataExporter(); 
		Genus lichen = Genus.getInstance();
		Object[][] Exdata = lichen.getExportResultTableData(); 

		if(Exdata[2][0] != null){
			JFileChooser chooser = new JFileChooser();
			String pictureFile = gui.fh.getLastDir();
			System.out.println(pictureFile);
			chooser.setSelectedFile(new File(pictureFile+".csv"));

			int returnVal = chooser.showSaveDialog(null); 
			String path;
			if(returnVal == JFileChooser.APPROVE_OPTION){
				try{ 
					path = chooser.getSelectedFile().getAbsolutePath(); 

					if(d.export(Exdata, path)){ 
						JOptionPane.showMessageDialog(null, "Erfolgreich exportiert");
					}else{ 
						JOptionPane.showMessageDialog(null, "Export Fehler");
					}

				}catch (NullPointerException e){
					System.err.println("no file choosen");
				} 
			}
		}else{
			JOptionPane.showMessageDialog(null, "Keine Daten vorhanden");
		}

	}
}