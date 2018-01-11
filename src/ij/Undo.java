/**Implements the Edit/Undo command.*/

package ij;
import ij.process.*;
import ij.gui.*;
import ij.measure.Calibration;

/** This class consists of static methods and
	fields that implement ImageJ's Undo command. */
public class Undo {

	public static final int NOTHING = 0;
	public static final int FILTER = 1;
	public static final int TYPE_CONVERSION = 2;
	public static final int PASTE = 3;
	public static final int COMPOUND_FILTER = 4;
	public static final int COMPOUND_FILTER_DONE = 5;
	public static final int TRANSFORM = 6;
	public static final int OVERLAY_ADDITION = 7;
	public static final int ROI = 8;
	
	private static int whatToUndo = NOTHING;
	private static int imageID;
	private static ImageProcessor ipCopy = null;
	private static ImagePlus impCopy;
	private static Calibration calCopy;
	private static Roi roiCopy;
	
	public static void setup(int what, ImagePlus imp) {
		if (imp==null) {
			whatToUndo = NOTHING;
			reset();
			return;
		}
		//IJ.log(imp.getTitle() + ": set up undo (" + what + ")");
		if (what==FILTER && whatToUndo==COMPOUND_FILTER)
				return;
		if (what==COMPOUND_FILTER_DONE) {
			if (whatToUndo==COMPOUND_FILTER)
				whatToUndo = what;
			return;
		}
		whatToUndo = what;
		imageID = imp.getID();
		if (what==TYPE_CONVERSION) {
			ipCopy = imp.getProcessor();
			calCopy = (Calibration)imp.getCalibration().clone();
		} else if (what==TRANSFORM) {	
			impCopy = new ImagePlus(imp.getTitle(), imp.getProcessor().duplicate());
		} else if (what==COMPOUND_FILTER) {
			ImageProcessor ip = imp.getProcessor();
			if (ip!=null)
				ipCopy = ip.duplicate();
			else
				ipCopy = null;
		} else if (what==OVERLAY_ADDITION) {
			impCopy = null;
			ipCopy = null;
		} else if (what==ROI) {
			impCopy = null;
			ipCopy = null;
			Roi roi = imp.getRoi();
			if (roi!=null) {
				roiCopy = (Roi)roi.clone();
				roiCopy.setImage(null);
			} else
				whatToUndo = NOTHING;
		} else
			ipCopy = null;
	}
		
	public static void reset() {
		if (whatToUndo==COMPOUND_FILTER || whatToUndo==OVERLAY_ADDITION)
			return;
		whatToUndo = NOTHING;
		imageID = 0;
		ipCopy = null;
		impCopy = null;
		calCopy = null;
		roiCopy = null;
		//IJ.log("Undo: reset");
	}


	static boolean swapImages(ImagePlus imp1, ImagePlus imp2) {
		if (imp1.getWidth()!=imp2.getWidth() || imp1.getHeight()!=imp2.getHeight()
		|| imp1.getBitDepth()!=imp2.getBitDepth() || IJ.macroRunning())
			return false;
		ImageProcessor ip1 = imp1.getProcessor();
		ImageProcessor ip2 = imp2.getProcessor();
		double min1 = ip1.getMin();
		double max1 = ip1.getMax();
		double min2 = ip2.getMin();
		double max2 = ip2.getMax();
		ip2.setSnapshotPixels(ip1.getPixels());
		ip2.swapPixelArrays();
		ip1.setPixels(ip2.getSnapshotPixels());
		ip2.setSnapshotPixels(null);
		ip1.setMinAndMax(min2, max2);
		ip2.setMinAndMax(min1, max1);
		return true;
	}

}
