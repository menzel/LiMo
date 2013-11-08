package de.thm.bi.recognition.segmentation;

import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.segmentation.algorithm.DBSCAN;
import de.thm.bi.utils.DataSetGenerator;

public class TestSegmenation {
	public static void main(String[] args) {

		final int minPoints = 6, epsilon = 10;

		DBSCAN dbscan = new DBSCAN(epsilon, minPoints);
		IDataSet dataSet = DataSetGenerator.generateRandomDataSet();
		dbscan.performSegmentation(dataSet);
	}
}
