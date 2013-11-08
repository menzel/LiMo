package de.thm.bi.recognition.normalize;

import ij.ImagePlus;
import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * Interface that defines methods for analyzing an Image
 * 
 * @author Artur Klos
 * 
 */
public interface INormalization {

	/**
	 * Attributes that may be assigned
	 * 
	 * @author Artur Klos
	 * 
	 */
	public enum Attributes {
		GRAYSCALE_INT, MAX_GRAYSCALE_INT, MIN_GRAYSCALE_INT, IMAGE_WIDTH, IMAGE_HEIGHT
	}

	/**
	 * Normalizes a picture and retrives all points that belong to a nuclei
	 * 
	 * @param img
	 *            Image to analzye
	 * @return Returns a pointslist of prostate cancer points
	 */
	public IDataSet normalize(ImagePlus img);
}
