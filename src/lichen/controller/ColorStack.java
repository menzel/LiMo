package lichen.controller;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Represents a list of different colors  
 * @author menzel
 *
 */
public class ColorStack {

private int i = 12;
private Color[] colorList = new Color[i];

	
	
	public ColorStack() { 
		
		colorList[1] = (Color.blue); 
		colorList[2] = (new Color(125,0,0)); //dunkelrot/ braun
		colorList[3] = (Color.green);
		colorList[4] = (Color.magenta);
		colorList[5] = (Color.orange); 
		colorList[6] = (Color.cyan); 
		colorList[7] = (new Color(0,91,0)); // dunkelgrün
		colorList[8] = Color.YELLOW; 
		colorList[10] = (Color.pink);
	}
	

	/**
	 * pops a color off the list until its empty, then gives the colors gray 
	 * @return
	 */
	public Color pop(){
		if(i == 0){
			return Color.gray; 
			//TODO: random color
			
		}else{ 
			i--;
			return this.colorList[colorList.length-i];
			
		}

		
	}

}
