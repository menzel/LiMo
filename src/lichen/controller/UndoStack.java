package lichen.controller;

import ij.process.ImageProcessor;

import java.util.ArrayList;

import de.thm.bi.recognition.data.Point;

public class UndoStack {
//	private ArrayList<int[][]> undoArrayList;
	
	private ArrayList<ArrayList<int[]>> undoPosList = new ArrayList<ArrayList<int[]>>();
	private int stackSize = 0;
	private int[] imp;
	private ImageProcessor ip;
	private int[] lastImp;
	private int lastImpPixelCount =0;
	
	/**
	 * 
	 * @param imp
	 */
	public UndoStack(ImageProcessor imp) {
		this.imp = (int[]) imp.getPixelsCopy();
		this.ip =imp;
		
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
	 * @param arr
	 */
	public void addLastImp(int[] arr, int pixelcount){
		this.lastImp = arr;
		this.lastImpPixelCount  = pixelcount;
	}

	
	/**
	 * 
	 * @return
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
	 * @return
	 */
	public int undo() { 
		
		if(lastImp != null)
			return undoAll();
			
		int count =0;
		
		if(stackSize > 0){
			for(int[] p: undoPosList.remove(stackSize-1)){
				ip.setPixel(p[0], p[1], imp[p[1]*ip.getWidth() + p[0]]); 
				count++; 
			} 
		}
		
		stackSize--;
		
		return count;
	}
	
	

}
