package de.thm.bi.recognition.filter;

import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;

/**
 * * Interface that describes a Method for filtering DataSets
 * 
 * @author Artur Klos
 */
public interface IPointFilter {
	public List<IPoint> filter(IDataSet dataSet);
}
