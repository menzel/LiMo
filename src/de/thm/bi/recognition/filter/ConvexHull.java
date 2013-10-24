package de.thm.bi.recognition.filter;

import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * @author Artur Klos Simply returns the ConvexHull Calculated by the DataSet
 */
public class ConvexHull implements IPointFilter {

	@Override
	public List<IPoint> filter(IDataSet dataSet) {
		return dataSet.getConvexHull();
	}

}
