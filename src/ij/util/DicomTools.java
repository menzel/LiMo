package ij.util;
import ij.*;
import ij.process.*;

/** DICOM utilities */
public class DicomTools {
	private static final int MAX_DIGITS = 5;
	private static String[] sliceLabels;

	private static ImageStack sortStack(ImageStack stack, String[] strings) {
		ImageProcessor ip = stack.getProcessor(1);
		ImageStack stack2 = new ImageStack(ip.getWidth(), ip.getHeight(), ip.getColorModel());
		for (int i=0; i<stack.getSize(); i++) {
			int slice = (int)Tools.parseDouble(strings[i].substring(strings[i].length()-MAX_DIGITS), 0.0);
			if (slice==0) return null;
			stack2.addSlice(sliceLabels[slice-1], stack.getPixels(slice));
		}
		stack2.update(stack.getProcessor(1));
		return stack2;
	}

	private static String[] getSortStrings(ImageStack stack, String tag) {
		double series = getSeriesNumber(getSliceLabel(stack,1));
		int n = stack.getSize();
		String[] values = new String[n];
		sliceLabels = new String[n];
		for (int i=1; i<=n; i++) {
			String tags = getSliceLabel(stack,i);
			if (tags==null) return null;
			sliceLabels[i-1] = tags;
			double value = getNumericTag(tags, tag);
			if (Double.isNaN(value)) {
				if (IJ.debugMode) IJ.log("  "+tag+"  tag missing in slice "+i);
				return null;
			}
			if (getSeriesNumber(tags)!=series) {
				if (IJ.debugMode) IJ.log("  all slices must be part of the same series");
				return null;
			}
			values[i-1] = toString(value, MAX_DIGITS) + toString(i, MAX_DIGITS);
		}
		return values;
	}

	private static String toString(double value, int width) {
		String s = "       " + IJ.d2s(value,0);
		return s.substring(s.length()-MAX_DIGITS);
	}
	
	private static String getSliceLabel(ImageStack stack, int n) {
		String info = stack.getSliceLabel(n);
		if ((info==null || info.length()<100) && stack.isVirtual()) {
			String dir = ((VirtualStack)stack).getDirectory();
			String name = ((VirtualStack)stack).getFileName(n);
			DICOM reader = new DICOM();
			info = reader.getInfo(dir+name);
			if (info!=null)
				info = name + "\n" + info;
		}
		return info;
	}

	/** Returns the value (as a string) of the specified DICOM tag id (in the form "0018,0050")
		of the specified image or stack slice. Returns null if the tag id is not found. */
	public static String getTag(ImagePlus imp, String id) {
		String metadata = null;
		if (imp.getStackSize()>1) {
			ImageStack stack = imp.getStack();
			String label = stack.getSliceLabel(imp.getCurrentSlice());
			if (label!=null && label.indexOf('\n')>0) metadata = label;
		}
		if (metadata==null)
			metadata = (String)imp.getProperty("Info");
		return getTag(metadata, id);
	}
	
	private static double getSeriesNumber(String tags) {
		double series = getNumericTag(tags, "0020,0011");
		if (Double.isNaN(series)) series = 0;
		return series;
	}

	private static double getNumericTag(String hdr, String tag) {
		String value = getTag(hdr, tag);
		if (value==null) return Double.NaN;
		int index3 = value.indexOf("\\");
		if (index3>0)
			value = value.substring(0, index3);
		return Tools.parseDouble(value);
	}

	private static String getTag(String hdr, String tag) {
		if (hdr==null) return null;
		int index1 = hdr.indexOf(tag);
		if (index1==-1) return null;
		//IJ.log(hdr.charAt(index1+11)+"   "+hdr.substring(index1,index1+20));
		if (hdr.charAt(index1+11)=='>') {
			// ignore tags in sequences
			index1 = hdr.indexOf(tag, index1+10);
			if (index1==-1) return null;
		}
		index1 = hdr.indexOf(":", index1);
		if (index1==-1) return null;
		int index2 = hdr.indexOf("\n", index1);
		String value = hdr.substring(index1+1, index2);
		return value;
	}

}

