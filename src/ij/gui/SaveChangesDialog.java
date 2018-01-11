package ij.gui;
import ij.IJ;
import java.awt.*;
import java.awt.event.*;

/** A modal dialog box with a one line message and
	"Don't Save", "Cancel" and "Save" buttons. */
public class SaveChangesDialog extends Dialog implements ActionListener, KeyListener {
	private Button dontSave, cancel, save;
	private boolean cancelPressed, savePressed;

    public void actionPerformed(ActionEvent e) {
		if (e.getSource()==cancel)
			cancelPressed = true;
		else if (e.getSource()==save)
			savePressed = true;
		closeDialog();
	}

    void closeDialog() {
		//setVisible(false);
		dispose();
	}

	public void keyPressed(KeyEvent e) { 
		int keyCode = e.getKeyCode(); 
		IJ.setKeyDown(keyCode); 
		if (keyCode==KeyEvent.VK_ENTER) 
			closeDialog(); 
		else if (keyCode==KeyEvent.VK_ESCAPE) { 
			cancelPressed = true; 
			closeDialog(); 
			IJ.resetEscape();
		} 
	} 

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}
