package lichen.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

class BborderActionListener implements ActionListener {
	/**
	 * 
	 */
	private final MainGUI mainGUI;

	/**
	 * @param mainGUI
	 */
	BborderActionListener(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	        final JFrame borderWidthChooser = new JFrame("Thallus Rahmendicke wählen");
	        borderWidthChooser.setSize(280, 20);
	        borderWidthChooser.setVisible(true);

	        JPanel panel = new JPanel(new GridLayout(2,2));

	        borderWidthChooser.add(panel);

	        final JTextField value = new JTextField(this.mainGUI.borderWidth + "");
	        JTextField valueLabel = new JTextField("Strichdicke in mm:");
	        final JTextField infoLabel = new JTextField("");

	        infoLabel.setEditable(false);
	        valueLabel.setEditable(false);

	        JButton done = new JButton("Wert setzen");

	        panel.add(valueLabel);
	        panel.add(value);
	        panel.add(infoLabel);
	        panel.add(done);

	        done.addActionListener(new ActionListener() {

	                @Override
	                public void actionPerformed(ActionEvent arg0) {
	                        try{

	                                BborderActionListener.this.mainGUI.borderWidth = Double.parseDouble(value.getText());
	                                borderWidthChooser.setVisible(false);

	                        }catch (IllegalArgumentException e){
	                                infoLabel.setText("ungültiger Wert");

	                        }
	                }
	        });
	}
}