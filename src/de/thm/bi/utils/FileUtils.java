/**
 * 
 */
package de.thm.bi.utils;

import ij.IJ;
import ij.ImagePlus;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Artur Klos
 * 
 */
public class FileUtils {

	public static final ImagePlus readImageFromPath(String path) throws IOException {
		return IJ.openImage(path);
	}

	public static final void writeToFile(ImagePlus img, String filePath) throws IOException {
		ImageIO.write(img.getBufferedImage(), "png", new File(filePath));
	}
}
