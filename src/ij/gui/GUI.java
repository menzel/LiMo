package ij.gui;
import java.awt.*;
import ij.*;

/** This class consists of static GUI utility methods. */
public class GUI {

	/** Positions the specified window in the center of the screen. */
	public static void center(Window w) {
		Dimension screen = IJ.getScreenSize();
		Dimension window = w.getSize();
		if (window.width==0)
			return;
		int left = screen.width/2-window.width/2;
		int top = (screen.height-window.height)/4;
		if (top<0) top = 0;
		w.setLocation(left, top);
	}
	
    static private Frame frame;

}
