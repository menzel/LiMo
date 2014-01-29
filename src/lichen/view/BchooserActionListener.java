package lichen.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.naming.NameNotFoundException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import lichen.controller.ColorMatcher;
import lichen.model.Genus;

/**
 * Chooser dialog for color to species assin after AutoAnalysis
 * @author menzel
 *
 */
class BchooserActionListener implements ActionListener {
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		final JFrame choose = new JFrame("Wähle Farben"); 
		choose.setLayout(new BorderLayout());
		choose.setSize(300, 400);
		final ColorMatcher matcher = new ColorMatcher(); 

		final Color[] colors; 
		colors = matcher.getColors();

		if(colors.length == 0){
			JOptionPane.showMessageDialog(null, "Keine Farben vorhanden");

		}else{ 

			final JTable chooseTable = new JTable(colors.length,3); 
			final JTextArea notice = new JTextArea("Bitte IDs eintragen");

			chooseTable.getTableHeader().setReorderingAllowed(false);
			chooseTable.getColumnModel().getColumn(0).setPreferredWidth(30);
			chooseTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
			chooseTable.getColumnModel().getColumn(2).setPreferredWidth(50); 

			chooseTable.getColumnModel().getColumn(2).setCellRenderer(new ResultsColorCellRenderer()); 

			JButton finish = new JButton("Fertig");
			JPanel choosePanel = new JPanel();
			chooseTable.setShowGrid(true);

			choosePanel.add(finish); 
			choosePanel.add(chooseTable); 

			choose.add(choosePanel, BorderLayout.CENTER);
			choose.add(notice, BorderLayout.SOUTH);
			choose.setVisible(true);


			/**
			 * Read user entered values and apply to data
			 */
			finish.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) { 
					chooseTable.setEnabled(false);
					chooseTable.clearSelection();

					for(int i =0; i< colors.length; i++){

						int id;
						if(chooseTable.getModel().getValueAt(i, 0) == null){
							id = Genus.getInstance().getSize();//get id of last lichen: "unbekannt"

						}else{ 
							id = Integer.parseInt(chooseTable.getModel().getValueAt(i,0).toString());
						}

						try {
							matcher.setColor(id, i);

							notice.setText("Zuweisung erfolgreich");
							choose.setVisible(false);

						} catch (NameNotFoundException e) {
							notice.setText("ID " + id + " wurde nicht gefunden");
							System.out.println("not found ");
						} 
					} 
				}
			}); 

		}
	}
}
