package lichen.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * Holds a list of buttons of which only one is active 
 * @author menzel
 *
 */
public class ButtonGroup {
	

	private ArrayList<JButton> list = new ArrayList<JButton>();
	private JButton activeButton;
	
	/**
	 * Adds a button to the list
	 * @param button button to be added
	 */
	public void add(final JButton button){
		list.add(button); 
	}
	
	/**
	 * highlights the active button with red font color
	 * makes others black
	 */
	public void highlight(){
		for(JButton button: list){
			if(button == activeButton){
				button.setForeground(Color.RED); 
			}else{
				button.setForeground(Color.black);
			}
		}
		
	}

	/**
	 * Sets the active Button 
	 * @param activeButton
	 */
	public void setActive(JButton activeButton) {
		this.activeButton  = activeButton;
		
	}
}
