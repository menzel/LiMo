package ij.macro;
import ij.*;
import ij.gui.ImageCanvas;

import java.awt.*;
import ij.plugin.frame.Editor;
																																																																																																																																																					   

/** This class runs macros in a separate thread. */
public class MacroRunner implements Runnable {

	private String macro;
	private Program pgm;
	private int address;
	private String name;
	private Thread thread;
	private String argument;
	private Editor editor;

	/** Create a MacrRunner. */
	public MacroRunner() {
	}

	/** Create a new object that interprets macro source in a separate thread. */
	public MacroRunner(String macro) {
		this(macro, (Editor)null);
	}

	/** Create a new object that interprets macro source in debug mode if 'editor' is not null. */
	public MacroRunner(String macro, Editor editor) {
		this.macro = macro;
		this.editor = editor;
		thread = new Thread(this, "Macro$"); 
		thread.setPriority(Math.max(thread.getPriority()-2, Thread.MIN_PRIORITY));
		thread.start();
	}

	/** Create a new object that interprets macro source in a 
		separate thread, and also passing a string argument. */
	public MacroRunner(String macro, String argument) {
		this.macro = macro;
		this.argument = argument;
		thread = new Thread(this, "Macro$"); 
		thread.setPriority(Math.max(thread.getPriority()-2, Thread.MIN_PRIORITY));
		thread.start();
	}

	/** Create a new object that runs a tokenized macro in a separate thread,
		passing a string argument. */
	public MacroRunner(Program pgm, int address, String name, String argument) {
		this.pgm = pgm;
		this.address = address;
		this.name = name;
		this.argument = argument;
		thread = new Thread(this, name+"_Macro$");
		thread.setPriority(Math.max(thread.getPriority()-2, Thread.MIN_PRIORITY));
		thread.start();
	}

	/** Create a new object that runs a tokenized macro in debug mode if 'editor' is not null. */
	public MacroRunner(Program pgm, int address, String name, Editor editor) {
		this.pgm = pgm;
		this.address = address;
		this.name = name;
		this.editor = editor;
		thread = new Thread(this, name+"_Macro$");
		thread.setPriority(Math.max(thread.getPriority()-2, Thread.MIN_PRIORITY));
		thread.start();
	}

	/** Runs tokenized macro on current thread if pgm.queueCommands is true. */
	public void runShortcut(Program pgm, int address, String name) {
		this.pgm = pgm;
		this.address = address;
		this.name = name;
		if (pgm.queueCommands)
			run();
		else {
			thread = new Thread(this, name+"_Macro$");
			thread.setPriority(Math.max(thread.getPriority()-2, Thread.MIN_PRIORITY));
			thread.start();
		}
	}
	
	public Thread getThread() {
		return thread;
	}

	public void run() {
		Interpreter interp = new Interpreter();
		interp.argument = argument;
		if (editor!=null)
			interp.setEditor(editor);
		try {
			if (pgm==null)
				interp.run(macro);
			else {
				if ("Popup Menu".equals(name)) {
					PopupMenu popup = Menus.getPopupMenu();
					if (popup!=null) {
						ImagePlus imp = null;
						Object parent = popup.getParent();
						if (parent instanceof ImageCanvas)
							imp = ((ImageCanvas)parent).getImage();
						if (imp!=null)
							WindowManager.setTempCurrentImage(Thread.currentThread(), imp);
					}
				}
				interp.runMacro(pgm, address, name);
			}
		} catch(Throwable e) {
			interp.abortMacro();
			IJ.showStatus("");
			IJ.showProgress(1.0);
			ImagePlus imp = WindowManager.getCurrentImage();
			if (imp!=null) imp.unlock();
			String msg = e.getMessage();
			if (e instanceof RuntimeException && msg!=null && e.getMessage().equals(Macro.MACRO_CANCELED))
				return;
			IJ.handleException(e);
		}
	}

}

