package lichen.view;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileHandler {

	private String lastdir = "";
	private static final int heapFactor = 80;

	/**
	 * Shows FileChooser Dialog to choose a picture
	 * @return - the picture
	 */
	public ImagePlus openImagePlus() throws Exception{

		final JFileChooser jc;

		if(lastdir.equals("")){ 
			jc = new JFileChooser("");
		}else{ 
			jc = new JFileChooser(lastdir);
		}

		LichenFileChooser ljc = new LichenFileChooser(jc);
		jc.addPropertyChangeListener(ljc);
		jc.setAccessory(ljc);

		int returnVal = jc.showDialog(null, "Wähle Bild");

		if(returnVal == JFileChooser.APPROVE_OPTION){


			Opener opener = new Opener();
			File file = new File(jc.getSelectedFile().toString());
			double heapSize = Runtime.getRuntime().maxMemory()/ (1024*1024);
			int returnValue = JOptionPane.NO_OPTION;
			
			/* check picture size*/
			if(file.length()/(1024*1024)*heapFactor > heapSize){ /* Image size bigger than maxMemory*heapFactor  */ 

				returnValue = askUser(); 

			}
			if(file.length()/(1024*1024)*heapFactor > heapSize*10){
				JOptionPane.showMessageDialog(MainGUI.getInstance(), "Das Bild ist viel zu groß für die Analyse\n"
						+ "Um es zu verkleinern muss ein externes Programm verwendet werden.", null, JOptionPane.ERROR_MESSAGE, null);
			
				
			}
			
			
			
			ImagePlus imp = opener.openImage(jc.getSelectedFile().toString());
			lastdir = jc.getSelectedFile().toString();
			
			if(imp == null){
				throw new NullPointerException("cannot load image");
			}
			
			//decrease size
			if(returnValue == JOptionPane.YES_OPTION){
				double fLengthM = ((file.length()/(1024*1024))*80);
				makeSmaller(imp, (int)(Math.round(fLengthM/heapSize)/2));
			}
			returnVal = JOptionPane.NO_OPTION;

			/* check picture size*/
			if(imp.getWidth()*imp.getHeight() > heapSize*15000){ /* Image dimensions bigger than maxMemory*150000 */ 
				returnValue = askUser(); 
			}
			
			//decrease dimensions
			if(returnValue == JOptionPane.YES_OPTION){
				double factor = 2; //Scale factor, works best here
				
				
				double width = imp.getWidth()/factor;
				double height = imp.getHeight()/factor;
				
				imp.setProcessor(imp.getProcessor().resize((int)width, (int)height));
			}
			
			//rotate Image if high side is up
			if(imp.getWidth() < imp.getHeight()){
				imp.setProcessor( imp.getProcessor().rotateRight());
			} 

			

			return imp; 
		}

		return null;

	}

	/**
	 * @return
	 */
	private int askUser() {
		int returnValue;
		returnValue = JOptionPane.showConfirmDialog(MainGUI.getInstance(), "Das Bild ist möglicherweise zu groß für den verfügbaren Arbeitsspeicher. Wenn " +
				"das Programm zu wenig Arbeitsspeicher hat, kann das zu " +
				"Problemen führen.\nDer maximale Arbeitsspeicher muss erhöht, oder die Größe des Bildes verkleinert werden.\n" +
				"Wie man den Arbeitsspeicher erhöht, steht im Benutzerhandbuch.\n" +
				"Soll versucht werden, dass Bild zu verkleinern?"
				, "Bildgröße", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		return returnValue;
	}

	/**
	 * Save an image to file
	 * @param imp imagePlus to be saved
	 * @param path - path/filename of the image
	 */
	public boolean saveImage(ImagePlus imp, String path){
		FileSaver filesaver = new FileSaver(imp);
		return filesaver.save(path); 
	}

	/**
	 * reloads the last image
	 * @return 
	 */
	public ImagePlus reloadImage() throws NullPointerException{
		Opener opener = new Opener();
		ImagePlus imp = opener.openImage(lastdir);

		if(imp == null){
			throw new NullPointerException("cannot load image");
		}

		if(imp.getWidth() < imp.getHeight()){
			imp.setProcessor( imp.getProcessor().rotateRight());
		}

		return imp;	
	} 
	public String getLastDir(){
		return this.lastdir;

	}

	public void setLastDir(String inipath) {
		this.lastdir = inipath;

	}

	/**
	 * shinks the imgage by factor n 
	 * @param imp - image to be shrinked
	 * @param n - shrink factor
	 */
	public void makeSmaller(ImagePlus imp, int sFactor){
		imp.setProcessor(imp.getProcessor().bin(sFactor)); 

	} 
}
