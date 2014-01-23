package lichen.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import lichen.controller.ColorStack;

final class BcolorListActionListener implements ActionListener {
	/**
	 * 
	 */
	private MainGUI gui;

	/**
	 * @param mainGUI
	 */
	BcolorListActionListener(MainGUI mainGUI) {
		gui = mainGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(gui.manualAnalyzer == null ){
			final JFrame choose = new JFrame("Wähle Farbreihenfolge"); 
			choose.setLayout(new BorderLayout());
			choose.setSize(200, 280);

			final Color[] colors = ColorStack.getAllColors(); 
			final JTable chooseTable = new JTable(colors.length,2); 
			final JTextArea notice = new JTextArea("Bitte Reihenfolge eintragen");
			notice.setEditable(false);

			chooseTable.getTableHeader().setReorderingAllowed(false);
			chooseTable.getColumnModel().getColumn(0).setPreferredWidth(30);
			chooseTable.getColumnModel().getColumn(1).setPreferredWidth(50); 

			chooseTable.getColumnModel().getColumn(1).setCellRenderer(new ChooserColorCellRenderer()); 

			JPanel buttonPanel  = new JPanel(new GridLayout(0, 2));
			JButton finish = MainGUI.createButton("Fertig");
			JButton cancel = MainGUI.createButton("Abbruch");

			buttonPanel.add(finish);
			buttonPanel.add(cancel);


			JPanel choosePanel = new JPanel(new BorderLayout());
			choosePanel.add(buttonPanel, BorderLayout.SOUTH);

			choosePanel.add(chooseTable, BorderLayout.CENTER); 

			choose.add(choosePanel, BorderLayout.CENTER);
			choose.add(notice, BorderLayout.SOUTH);
			choose.setVisible(true);

			/**
			 * Cancels the insertion and does nothing to color stack
			 */
			cancel.addActionListener(new ActionListener() {


				@Override
				public void actionPerformed(ActionEvent e) {

					choose.setVisible(false); 
					choose.setEnabled(false);
				}
			});

			/**
			 * Read user entered values and apply to ColorStack 
			 */
			finish.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					chooseTable.setEnabled(false);
					chooseTable.clearSelection();
					Color newColors[] = new Color[11];

					for(int i =0; i< colors.length; i++){

						try{ 
							Object o =chooseTable.getModel().getValueAt(i, 0); 
							newColors[Integer.parseInt(o.toString())] = colors[i]; 

						}catch(NullPointerException n){ 
							n.printStackTrace();
							//No Id inserted, get value somehow 
						}catch(IllegalArgumentException n){ 
							n.printStackTrace();
							//Id out of range 
						}
					} 
					ColorStack.setColorPos(newColors);
					choose.setVisible(false);
				}
			}); 

		}else{

			JOptionPane.showMessageDialog(null, "Die Wahl der Farbreihenfolge ist nur vor dem Starten der manuellen Analyse möglich");
		}
	}
}