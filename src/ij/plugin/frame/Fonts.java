package ij.plugin.frame;
import java.awt.*;
import java.awt.event.*;

import ij.*;
import ij.gui.*;

/** Displays a window that allows the user to set the font, size and style. */
public class Fonts extends PlugInFrame implements PlugIn, ItemListener {

	public static final String LOC_KEY = "fonts.loc";
	private static String[] sizes = {"8","9","10","12","14","18","24","28","36","48","60","72","100","150","225","350"};
	private static int[] isizes = {8,9,10,12,14,18,24,28,36,48,60,72,100,150,225,350};
	private Choice font;
	private Choice size;
	private Choice style;
	private Checkbox checkbox;
	private static Frame instance;

	int getSizeIndex() {
		int size = TextRoi.getSize();
		int index=0;
		for (int i=0; i<isizes.length; i++) {
			if (size>=isizes[i])
				index = i;
		}
		return index;
	}
	
	public void itemStateChanged(ItemEvent e) {
		String fontName = font.getSelectedItem();
		int fontSize = Integer.parseInt(size.getSelectedItem());
		String styleName = style.getSelectedItem();
		int fontStyle = Font.PLAIN;
		int justification = TextRoi.LEFT;
		if (styleName.endsWith("Bold"))
			fontStyle = Font.BOLD;
		else if (styleName.equals("Italic"))
			fontStyle = Font.ITALIC;
		else if (styleName.equals("Bold+Italic"))
			fontStyle = Font.BOLD+Font.ITALIC;
		if (styleName.startsWith("Center"))
			justification = TextRoi.CENTER;
		else if (styleName.startsWith("Right"))
			justification = TextRoi.RIGHT;
		TextRoi.setFont(fontName, fontSize, fontStyle, checkbox.getState());
		TextRoi.setGlobalJustification(justification);
		IJ.showStatus(fontSize+" point "+fontName + " " + styleName);
	}

}
