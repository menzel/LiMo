package lichen.controller;

import ij.process.ImageProcessor; 
import java.util.ArrayList;


/**
 * Represents a stack of the last changes as well as the last picture for first undo
 * @author menzel
 *
 */
public class UndoStack {

	private ArrayList<ArrayList<int[]>> undoPosList = new ArrayList<ArrayList<int[]>>();
	private int stackSize = 0;
	private int[] pixels;
	private ImageProcessor ip;
	private int[] lastImp;
	private int lastImpPixelCount =0;

	/**
	 * Contructor
	 * @param ip ImageProcesser of the Image to be analyzed
	 */
	public UndoStack(ImageProcessor ip) {
		try{ 
			this.pixels = (int[]) ip.getPixelsCopy();

		}catch(ClassCastException c){

			byte[] barr = (byte[]) ip.getPixels();
			this.pixels = new int[barr.length];

			for(int i = 0; i < barr.length; i++){
				if(barr[i] == 0){
					this.pixels[i]  = 0;
				}else{
					this.pixels[i]  = -1;
				}
			} 	
		}
		this.ip =ip;

	}

	/**
	 * Adds a new undo Position to the stack
	 * @param undoPos a list of int[] which hold 
	 */
	public void add(ArrayList<int[]> undoPos){
		this.undoPosList.add(undoPos); 
		stackSize++;
	}

	/**
	 * 
	 * Stores the last Image (as int[] ) for first undo
	 * @param arr - int[] of pixels for floodfill undo
	 * @param pixelcount - last filled pixelcount
	 */
	public void addLastImp(int[] arr, int pixelcount){
		this.lastImp = arr;
		this.lastImpPixelCount  = pixelcount;
	}


	/**
	 * first undo, restores picture from lastImp, not from undoPosList 
	 * @return pixelcount substracted
	 */
	private int undoAll(){ 
		ip.setPixels(lastImp);

		int p = lastImpPixelCount;
		lastImpPixelCount = 0;


		lastImp = null;

		return p; 
	}

	/**
	 * undo a flood fill from the list, undo with undoAll if possible
	 * @return - pixelcount of unfilled pixels
	 */
	public int undo() { 
		
		if(lastImp != null){ 

			undoPosList.remove(stackSize-1);

			stackSize--; 
			return undoAll();
		}

		int count =0;

		if(stackSize > 0){
			for(int[] p: undoPosList.remove(stackSize-1)){
				ip.setPixel(p[0], p[1], pixels[p[1]*ip.getWidth() + p[0]]); 
				count++; 
			} 
		}

		stackSize--;

		return count;
	}
	
	/**
	 * Returns the size of the last flood fill without removing it from the list
	 * @return size 
	 */
	public int getLastUndoCount(){
		
		ArrayList<int[]> p;
		
		if(stackSize > 0){
			p = undoPosList.remove(stackSize-1); 
			undoPosList.add(p);
			
			return p.size();
		} 
		
		return 0;
	}

}
