package lichen.controller;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Represents a static list of different colors  
 * @author menzel
 *
 */
public class ColorStack {

	private static int i = 12;
	private static Color[] colorList = new Color[i];
 

	/**
	 * Inits all colors to the list, or restores them
	 */
	public static void ColorStackInit() { 

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
	public static void setColorPos(Color color, int parseInt) {
		
		if(parseInt < 1 || parseInt > i){
	//		throw new IllegalArgumentException; 
		}
		colorList[parseInt] = color;

	}

}
