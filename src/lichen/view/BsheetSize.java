package lichen.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import lichen.controller.Processor;

final class BsheetSize implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) { 

		final JFrame borderWidthChooser = new JFrame("Foliengröße wählen");
		borderWidthChooser.setSize(400, 120);
		borderWidthChooser.setVisible(true);

		JPanel ovPanel = new JPanel(new BorderLayout()); 
		JPanel panel = new JPanel(new GridLayout(2,2)); 

		ovPanel.add(panel, BorderLayout.CENTER);
		borderWidthChooser.add(ovPanel);

		final JTextField value = new JTextField("");

		final JTextArea infoLabel = new JTextArea("Bitte die Länge der langen Seite in mm eintragen,\noder"
				+ " eine Voreinstellung auswählen:");
		infoLabel.setPreferredSize(new Dimension(400, 35));

		infoLabel.setEditable(false);
		infoLabel.setEnabled(false);

		infoLabel.setBackground(Color.gray);
		infoLabel.setDisabledTextColor(Color.white);

		JButton dina4 = new JButton("DinA4");
		JButton done = new JButton("Wert setzen");
		JButton dina3 = new JButton("DinA3");

		ovPanel.add(infoLabel, BorderLayout.NORTH);

		panel.add(value); 
		panel.add(dina4);
		panel.add(done);
		panel.add(dina3);

		done.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{ 
					double lw = Double.parseDouble(value.getText());
					Processor.setSheetWidth(lw); 
					borderWidthChooser.setVisible(false);

				}catch (IllegalArgumentException e){
					infoLabel.setText("ungültiger Wert"); 
				} 
			}
		});

		dina3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{ 
					Processor.setSheetWidth(420); 

					borderWidthChooser.setVisible(false);

				}catch (IllegalArgumentException e){
					infoLabel.setText("ungültiger Wert");

				} 
			}
		});

		dina4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{ 
					Processor.setSheetWidth(297); 

					borderWidthChooser.setVisible(false);

				}catch (IllegalArgumentException e){
					infoLabel.setText("ungültiger Wert"); 
				} 
			}
		}); 

	}
}