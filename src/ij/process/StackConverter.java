package ij.process;

import java.awt.*;

import ij.*;

/** This class does stack type conversions. */
public class StackConverter {
	ImagePlus imp;
	int type, nSlices, width, height;

    /** Converts an RGB or 8-bit color stack to 8-bit grayscale. */
	void convertRGBToGray8() {
		ImageStack stack1 = imp.getStack();
		ImageStack stack2 = new ImageStack(width, height);
		ImageProcessor ip;
		Image img;
		String label;
	    int inc = nSlices/20;
	    if (inc<1) inc = 1;
		for(int i=1; i<=nSlices; i++) {
			label = stack1.getSliceLabel(1);
			ip = stack1.getProcessor(1);
			stack1.deleteSlice(1);
			if (ip instanceof ByteProcessor)
				ip = new ColorProcessor(ip.createImage());
			boolean scale = ImageConverter.getDoScaling();
			stack2.addSlice(label, ip.convertToByte(scale));
			if ((i%inc)==0) {
				IJ.showProgress((double)i/nSlices);
				IJ.showStatus("Converting to 8-bits: "+i+"/"+nSlices);
			}
		}
		imp.setStack(null, stack2);
		IJ.showProgress(1.0);
	}

}
