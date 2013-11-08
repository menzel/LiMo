/**
 * 
 */
package de.thm.bi.recognition.filter;

import java.util.ArrayList;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.normalize.INormalization.Attributes;

/**
 * Filters the brightest points by grayscale
 * 
 * @author Artur Klos
 * 
 */
public class Grayscale implements IPointFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dbpcanalyzer.ak.clustering.data.IPointFilter#filter(dbpcanalyzer.ak.
	 * clustering.data.dataset.IDataSet)
	 */
	@Override
	public List<IPoint> filter(IDataSet dataSet) {
		ArrayList<IPoint> result = new ArrayList<IPoint>(100);
		List<IPoint> points = dataSet.getAllPoints();

		int grayscale = (int) (dataSet.hasStoredAttribute(Attributes.MAX_GRAYSCALE_INT) ? dataSet
				.getStoredAttribute(Attributes.MAX_GRAYSCALE_INT) : 255);
		int minGrayscale = (int) (dataSet.hasStoredAttribute(Attributes.MIN_GRAYSCALE_INT) ? dataSet
				.getStoredAttribute(Attributes.MIN_GRAYSCALE_INT) : 0);
		int limit = points.size();
		boolean pointsFound = false;

		while (minGrayscale <= grayscale && !pointsFound) {
			for (int i = 0; i < limit; i++) {
				IPoint point = points.get(i);
				if (((int) point.getStoredAttribute(Attributes.GRAYSCALE_INT)) >= grayscale) {
					pointsFound = true;
					result.add(point);
				}
			}
			grayscale--;
		}

		return result;
	}
}
