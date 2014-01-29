package lichen.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;

/**
 * Holds a list of buttons of which only one is active
 * used for highlighting this button
 * @author menzel
 *
 */
public class ButtonGroup {
	

	private Collection<JButton> list = new ArrayList<JButton>();
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
