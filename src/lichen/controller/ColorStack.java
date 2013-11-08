package lichen.controller;

import java.awt.Color;

import lichen.view.MainGUI;

/**
 * Represents a static list of different colors  
 * @author menzel
 *
 */
public class ColorStack {

	private static int i = 11;
	private static Color[] colorList = new Color[i-1];
 

	/**
	 * Inits all colors to the list, or restores them
	 */
	public static void ColorStackInit() { 
		
		colorList[0] = new Color(255,5,5);
		colorList[1] = (Color.blue); 
		colorList[2] = (new Color(125,0,0)); //dunkelrot/ braun
		colorList[3] = (Color.green);
		colorList[4] = (Color.magenta);
		colorList[5] = (Color.orange); 
		colorList[6] = (Color.cyan); 
		colorList[7] = (new Color(0,91,0)); // dunkelgrün
		colorList[8] = Color.YELLOW; 
		colorList[9] = (Color.pink);
	}


	/**
	 * pops a color off the list until its empty, then gives the colors gray 
	 * @return
	 */
	public static Color pop(){
		
		i--; 
		if(i < 1){
			
			return new Color((int)(Math.random()*170+30), (int)(Math.random()*170+30), (int)(Math.random()*170+30)); 

		}else{ 
			return ColorStack.colorList[colorList.length-i];

		} 

	}

	/**
	 * Returns the whole color list
	 * @return
	 */
	public static Color[] getAllColors(){
		return colorList;
	}


	/**
	 * For setting an own color order
	 * @param color - color to be added
	 * @param parseInt - position in list
	 * 
	 * Do not use 0
	 */
	public static void setColorPos(Color color[]) {
		colorList = color;
	
	}
	
	public static void resetStack(){
		i = 11;
	}

}
