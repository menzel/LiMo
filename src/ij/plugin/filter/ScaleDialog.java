package ij.plugin.filter;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.measure.*;
import ij.util.Tools;
import ij.io.FileOpener;
import java.awt.*;
import java.awt.event.*;

class SetScaleDialog extends GenericDialog {
	static final String NO_SCALE = "<no scale>";
	String initialScale;
	Button unscaleButton;
	String length;
	boolean scaleChanged;

	public SetScaleDialog(String title, String scale, String length) {
		super(title);
		initialScale = scale;
		this.length = length;
	}

    public void textValueChanged(TextEvent e) {
		Object source = e.getSource();
		if (source==numberField.elementAt(0) || source==numberField.elementAt(1))
			scaleChanged = true;
 		Double d = getValue(((TextField)numberField.elementAt(0)).getText());
 		if (d==null)
 			{setScale(NO_SCALE); return;}
 		double measured = d.doubleValue();
 		d = getValue(((TextField)numberField.elementAt(1)).getText());
 		if (d==null)
 			{setScale(NO_SCALE); return;}
 		double known = d.doubleValue();
 		String theScale;
 		String unit = ((TextField)stringField.elementAt(0)).getText();
 		boolean noUnit = unit.startsWith("pixel")||unit.startsWith("Pixel")||unit.equals("");
 		if (known>0.0 && noUnit && e.getSource()==numberField.elementAt(1)) {
 			unit = "unit";
			((TextField)stringField.elementAt(0)).setText(unit);
 		}
 		boolean noScale = measured<=0||known<=0||noUnit;
 		if (noScale)
 			theScale = NO_SCALE;
 		else {
 			double scale = measured/known;
			int digits = Tools.getDecimalPlaces(scale, scale);
 			theScale = IJ.d2s(scale,digits)+(scale==1.0?" pixel/":" pixels/")+unit;
 		}
 		setScale(theScale);
	}
	
	public void actionPerformed(ActionEvent e) { 
		super.actionPerformed(e);
		if (e.getSource()==unscaleButton) {
			((TextField)numberField.elementAt(0)).setText(length);
			((TextField)numberField.elementAt(1)).setText("0.00");
			((TextField)numberField.elementAt(2)).setText("1.0");
			((TextField)stringField.elementAt(0)).setText("pixel");
			setScale(NO_SCALE);
			scaleChanged = true;
			if (IJ.isMacOSX())
				{setVisible(false); setVisible(true);}
		}
	}

	void setScale(String theScale) {
 		((Label)theLabel).setText("Scale: "+theScale);
	}

}
