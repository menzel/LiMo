package ij.process;

import java.util.ArrayList;

import lichen.controller.FloodFiller;
import lichen.view.MainGUI;

public class MyFloodFiller extends FloodFiller {


	private boolean fillAgain(int x, int y) {

		ArrayList<Point>n = new ArrayList<Point>();

		n.add(new Point(x, y));
		Point p;
		int color = ip.getPixel(x, y);

		MainGUI gui = MainGUI.getInstance(); 
		ip.setColor(gui.makeColor((int)(Math.random()*170+30), (int)(Math.random()*170+30), (int)(Math.random()*170+30))); 

		while(!n.isEmpty()){

			p= n.remove(n.size()-1);

			if(ip.getPixel(p.x, p.y) == color){

				ip.drawPixel(p.x, p.y); 
//				org.drawPixel(p.x,p.y);
				pixelcount++;

				n.add(new Point(p.x, p.y+1));
				n.add(new Point(p.x, p.y-1));

				n.add(new Point(p.x+1, p.y));
				n.add(new Point(p.x-1, p.y));
			}

		} 

		return true; 
	}

}
