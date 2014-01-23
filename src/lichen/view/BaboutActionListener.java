package lichen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

final class BaboutActionListener implements ActionListener {
	/**
	 * 
	 */
	private MainGUI gui;

	/**
	 * @param mainGUI
	 */
	BaboutActionListener(MainGUI mainGUI) {
		gui = mainGUI;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFrame aboutFrame = new JFrame("über Flechtenanalyse");
		//				aboutFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 
		aboutFrame.setSize(700, 400);
		String text = "Autor: Michael Menzel, Fachbereich MNI Technische Hochschule Mittelhessen\n\n" +
				"Im Auftrag von Prof. Dr. Ute Windisch, Fachbereich KMUB (THM) \n\n" +
				"Flechtenanalyseprogramm, Versionsnr: " + MainGUI.getVersion() + "\n"
				+ "Erstellungsdatum: " + MainGUI.getDate()  + "\n\n"+
				"Das Programm steht unter:\nGNU General Public License, version 3 (GPL-3.0)\n" +
				"http://opensource.org/licenses/GPL-3.0\n" +
				"LiMo-Analyse basiert auf dem Bildanalyseprogramm ImageJ: http://rsbweb.nih.gov/ij/index.html\n" +
				"\n\n" +
				" Copyright (C) <2013>  <Michael Menzel>\n This program is free software: you can redistribute it and/or modify it\n " +
				"under the terms of the GNU General Public License as published by the Free Software Foundation, \n" +
				"either version 3 of the License, or (at your option) any later version. \n" +
				"This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; \n" +
				"without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  \n" +
				"See the GNU General Public License for more details. \n" +
				"You should have received a copy of the GNU General Public License along with this program. \n" +
				" If not, see <http://www.gnu.org/licenses/>.\n";
		JTextArea aboutText = new JTextArea(text);
		aboutText.setEditable(false);
		aboutFrame.add(aboutText);
		aboutFrame.setVisible(true);
	}
}