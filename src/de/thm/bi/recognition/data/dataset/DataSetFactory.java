package de.thm.bi.recognition.data.dataset;

import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.PointsMatrix;

/**
 * Factory for creating DataSets
 * 
 * @author Artur Klos
 * 
 */
public class DataSetFactory {

	public enum DataSetTypes {
		DINSTANCE_LIST_DATA_SET, MATRIX_DATA_SET, ON_THE_FLY_DATA_SET
	}

	public static final DataSetTypes DEFAULT_DATA_SET = DataSetTypes.MATRIX_DATA_SET;

	/**
	 * Creates a implementation of the IDataSet interface. The types are defined
	 * by the enumeration DataSetTypes.
	 * 
	 * @param dataSet
	 *            DataSet type that should be created
	 * @param points
	 *            List of IPoints
	 * @return Returns a implementation of the IDataSet interface
	 */
	public static final IDataSet createDataSet(DataSetTypes dataSet, List<IPoint> points) {
		switch (dataSet) {
		case DINSTANCE_LIST_DATA_SET:
			return new DistanceList(points);
		case MATRIX_DATA_SET:
			return new Matrix(points);
		case ON_THE_FLY_DATA_SET:
			return new OnTheFly(points);
		}
		return null;
	}

	/**
	 * Creates a implementation of the IDataSet interface. The types are defined
	 * by the enumeration DataSetTypes.
	 * 
	 * @param dataSet
	 *            DataSet type that should be created
	 * @param points
	 *            List of IPoints
	 * @param matrix
	 *            PointsMatrix that should be reused
	 * @return Returns a implementation of the IDataSet interface
	 */
	public static final IDataSet createDataSet(DataSetTypes dataSet, List<IPoint> points, PointsMatrix matrix) {
		switch (dataSet) {
		case DINSTANCE_LIST_DATA_SET:
			return new DistanceList(points, matrix);
		case MATRIX_DATA_SET:
			return new Matrix(points, matrix);
		case ON_THE_FLY_DATA_SET:
			return new OnTheFly(points, matrix);
		}
		return null;
	}

	/**
	 * Creates the currently set default DataSet.
	 * 
	 * @param points
	 *            List of IPoints
	 * @return Returns a implementation of the IDataSet interface
	 */
	public static final IDataSet createDefault(List<IPoint> points) {
		return createDataSet(DEFAULT_DATA_SET, points);
	}

	/**
	 * Creates the currently set default DataSet
	 * 
	 * @param points
	 *            List of IPoints
	 * @param matrix
	 *            PointsMatrix that should be reused
	 * @return Returns a implementation of the IDataSet interface
	 */
	public static final IDataSet createDefault(List<IPoint> points, PointsMatrix matrix) {
		return createDataSet(DEFAULT_DATA_SET, points, matrix);
	}

	private DataSetFactory() {
	}

}
