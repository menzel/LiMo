/**
 * 
 */
package de.thm.bi.recognition.consoletools;

import java.io.IOException;
import java.util.List;

import de.thm.bi.recognition.data.IPoint;
import de.thm.bi.recognition.data.dataset.IDataSet;
import de.thm.bi.recognition.data.region.IRegion;
import de.thm.bi.recognition.normalize.NormalizationFactory;
import de.thm.bi.recognition.normalize.NormalizationFactory.ImageAnalyzerStrategies;
import de.thm.bi.recognition.segmentation.algorithm.ColorSegmentation;
import de.thm.bi.utils.FileUtils;
import de.thm.bi.utils.Preferences;

/**
 * @author Artur Klos
 * 
 */
public class ColorSegmentationTool {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String inputpath = "/home/menzel/Desktop/THM/4.Semester/flechtenkartierung/f.jpg";

		// EINSTELLUNGEN MÜSSEN INITIERT WERDEN
		Preferences.init();

		// HIER KANNST DU AUCH EINFACH EIN IMAGEPLUS BILD REINGEBEN STATT ES
		// SELBST MIT FILEUTILS ZU LADEN
		IDataSet dataSet = NormalizationFactory.create(ImageAnalyzerStrategies.IMAGEJ).normalize(
				FileUtils.readImageFromPath(inputpath));
		ColorSegmentation colorSegmentation = new ColorSegmentation();
		colorSegmentation.performSegmentation(dataSet);

		// MIT DATASET KANN GEARBEITET WERDEN...
		// HIER WURDE BEREITS VERSUCHT GLEICHE FARBEN IN EIN CLUSTER ZU PACKEN
		// WIE DAS FUNKTIONIERT KANNST DU IN DER KLASSE COLORSEGMENTATION
		// NACHGUCKEN SCHREIB MIR WENN ETWAS UNKLAR IST
		// CLUSTERS == REGIONEN HABS FRÜHER CLUSTERS GENANNT.. SOLLTE ICH AUCH
		// NOCH REFACTORN ^^

		List<IRegion> regions = dataSet.getClusters();

		int limit = regions.size();

		for (int i = 0; i < limit; i++) {
			IRegion region = regions.get(i);
			List<IPoint> points = region.getAllPoints();

			// REGIONEN SIND VON DATASET ABGELEITET DAHER KANNST DU AUF SIE
			// EBENFALLS EINE SEGMENTIERUNG ANWENDEN... FALLS ES DIR ETWAS
		//	colorSegmentation.performSegmentation(region);
			// usw
		}
	}
}
