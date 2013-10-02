/**
 * 
 */
package de.thm.bi.recognition.consoletools;

import java.io.IOException;
import java.util.List;

import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.normalize.NormalizationFactory;
import de.thm.bi.recognition.normalize.NormalizationFactory.ImageAnalyzerStrategies;
import de.thm.bi.recognition.segmentation.algorithm.DBSCAN;
import de.thm.bi.utils.FileUtils;
import de.thm.bi.utils.Preferences;

/**
 * @author Artur Klos
 * 
 */
public class DBSCANSegmentation {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String inputpath = "DATEIPFAD";
		// EINSTELLUNGEN MÜSSEN LEIDER INITIERT WERDEN
		Preferences.init();
		// HIER KANNST DU AUCH EINFACH EIN IMAGEPLUS BILD REINGEBEN STATT ES
		// SELBST MIT FILEUTILS ZU LADEN
		IDataSet dataSet = NormalizationFactory.create(ImageAnalyzerStrategies.IMAGEJ).normalize(
				FileUtils.readImageFromPath(inputpath));
		DBSCAN dbscan = new DBSCAN();
		dbscan.setEpsilon(2f);
		dbscan.setMinPoints(10);
		dbscan.performSegmentation(dataSet);
		// MIT DATASET KANN GEARBEITET WERDEN
		// CLUSTERS == REGIONEN HABS FRÜHER CLUSTERS GENANNT.. SOLLTE ICH AUCH
		// NOCH REFACTORN ^
		List<IRegion> regions = dataSet.getClusters();

		int limit = regions.size();

		for (int i = 0; i < limit; i++) {
			IRegion region = regions.get(i);
			region.getAllPoints();
			// usw
		}
	}
}
