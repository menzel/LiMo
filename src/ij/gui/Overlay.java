package ij.gui;
import java.awt.*;
import java.util.Vector;

/** An Overlay is a list of Rois that can be drawn non-destructively on an Image. */
public class Overlay {
	private Vector list;
    private boolean label;
    private boolean drawNames;
    private boolean drawBackgrounds;
    private Color labelColor;
    private Font labelFont;
    
    /** Constructs an empty Overlay. */
    public Overlay() {
    	list = new Vector();
    }
    
    /** Constructs an Overlay and adds the specified Roi. */
    public Overlay(Roi roi) {
    	list = new Vector();
    	list.add(roi);
    }

    /** Adds an Roi to this Overlay. */
    public void add(Roi roi) {
    	list.add(roi);
    }
    
    /* Adds a Shape to this Overlay. */
    //public void add(Shape shape, Color color, BasicStroke stroke) {
	//	Roi roi = new ShapeRoi(shape);
	//	roi.setStrokeColor(color);
	//	roi.setStroke(stroke);
    //	list.add(roi);
    //}

    /* Adds a String to this Overlay. */
    //public void add(String text, int x, int y, Color color, Font font) {
	//	TextRoi roi = new TextRoi(x, y-font.getSize(), text, font);
	//	roi.setStrokeColor(color);
	//	list.add(roi);
    //}

    /** Removes the Roi with the specified index from this Overlay. */
    public void remove(int index) {
    	list.remove(index);
    }

    /** Returns the Roi with the specified index. */
    public Roi get(int index) {
    	return (Roi)list.get(index);
    }

    /** Returns 'true' if this Overlay contains the specified Roi. */
    public boolean contains(Roi roi) {
    	return list.contains(roi);
    }

    /** Returns the number of Rois in this Overlay. */
    public int size() {
    	return list.size();
    }
    
    /** Returns on array containing the Rois in this Overlay. */
    public Roi[] toArray() {
    	Roi[] array = new Roi[list.size()];
    	return (Roi[])list.toArray(array);
    }

    /** Returns the bounds of this overlay. */
    /*
    public Rectangle getBounds() {
    	if (size()==0)
    		return new Rectangle(0,0,0,0);
    	int xmin = Integer.MAX_VALUE;
    	int xmax = -Integer.MAX_VALUE;
    	int ymin = Integer.MAX_VALUE;
    	int ymax = -Integer.MAX_VALUE;
		Roi[] rois = toArray();
		for (int i=0; i<rois.length; i++) {
			Rectangle r = rois[i].getBounds();
			if (r.x<xmin) xmin = r.x;
			if (r.y<ymin) ymin = r.y;
			if (r.x+r.width>xmax) xmax = r.x+r.width;
			if (r.y+r.height>ymax) ymax = r.y+r.height;
		}
		return new Rectangle(xmin, ymin, xmax-xmin, ymax-ymin);
	}
	*/

    /** Draws outlines of the Rois in this Overlay on the specified
    	ImageProcessor using the current color and line width of 'ip'. */
    //public void draw(ImageProcessor ip) {
	//	Roi[] rois = toArray();
	//	for (int i=0; i<rois.length; i++)
	//		rois[i].drawPixels(ip);
	//}

    public String toString() {
    	return list.toString();
    }
    
    public void drawLabels(boolean b) {
    	label = b;
    }
    
    public boolean getDrawLabels() {
    	return label;
    }
    
    public void drawNames(boolean b) {
    	drawNames = b;
		Roi[] rois = toArray();
		for (int i=0; i<rois.length; i++)
			rois[i].setIgnoreClipRect(drawNames);
    }
    
    public boolean getDrawNames() {
    	return drawNames;
    }

    public void drawBackgrounds(boolean b) {
    	drawBackgrounds = b;
    }
    
    public boolean getDrawBackgrounds() {
    	return drawBackgrounds;
    }

    public void setLabelColor(Color c) {
    	labelColor = c;
    }
    
    public Color getLabelColor() {
    	return labelColor;
    }

    public void setLabelFont(Font font) {
    	labelFont = font;
    }
    
    public Font getLabelFont() {
    	//if (labelFont==null && labelFontSize!=0)
    	//	labelFont = new Font("SansSerif", Font.PLAIN, labelFontSize);
    	return labelFont;
    }

    void setVector(Vector v) {list = v;}

}
