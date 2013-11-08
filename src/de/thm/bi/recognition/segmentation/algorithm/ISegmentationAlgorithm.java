package de.thm.bi.recognition.segmentation.algorithm;

import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * @author Artur Klos
 * 
 *         Interface that describes the methods of a cluster algorithm
 * 
 */
public interface ISegmentationAlgorithm {

	/**
	 * 
	 * The size source for subclustering
	 * 
	 * @author Artur Klos
	 * 
	 */
	public enum SizeSource {
		MEDIAN, AVERAGE, PRESET
	}

	/**
	 * @param dataSet
	 *            DataSet that the clustering should be performed on
	 */
	public void performSegmentation(IDataSet dataSet);
}
