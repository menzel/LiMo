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
	
	public UndoStack(ImageProcessor imp) {
		this.imp = (int[]) imp.getPixelsCopy();
		this.ip =imp;
		
	}
	
	public void add(ArrayList<int[]> undoPos){
		this.undoPosList.add(undoPos); 
		stackSize++;
	}

	/**
	 * 
	 * @return
	 */
	public int undo() { 
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
