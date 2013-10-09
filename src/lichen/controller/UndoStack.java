package lichen.controller;

import ij.process.ImageProcessor; 
import java.util.ArrayList;


public class UndoStack {

	private ArrayList<ArrayList<int[]>> undoPosList = new ArrayList<ArrayList<int[]>>();
	private int stackSize = 0;
	private int[] pixels;
	private ImageProcessor ip;
	private int[] lastImp;
	private int lastImpPixelCount =0;

	/**
	 * 
	 * @param ip
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
	 * 
	 * @param undoPos
	 */
	public void add(ArrayList<int[]> undoPos){
		this.undoPosList.add(undoPos); 
		stackSize++;
	}

	/**
	 * 
	 * @param arr - int[] of pixels for floodfill undo
	 * @param pixelcount - last filled pixelcount
	 */
	public void addLastImp(int[] arr, int pixelcount){
		this.lastImp = arr;
		this.lastImpPixelCount  = pixelcount;
	}


	/**
	 * 
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
	 * 
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



}
