package ij.process;

/** A table for easier downsizing by convolution with a kernel.
 *	Supports the interpolation methods of ImageProcessor: none, bilinear, bicubic
 *	Convention used: The left edges of the first pixel are the same for source and destination.
 *	E.g. when downsizing by a factor of 2, pixel 0 of the destination
 *	takes the space of pixels 0 and 1 of the source.
 *
 *	Example for use: Downsizing row 0 of 'pixels' from 'roi.width' to 'destinationWidth'.
 *	The input range is given by the roi rectangle.
 *	Output is written to row 0 of 'pixels2' (width: 'destinationWidth')
 <code>
	DownSizeTable dt = new DownSizeTable(width, roi.x, roi.width, destinationWidth, ImageProcessor.BICUBIC);
	int tablePointer = 0;
	for (int srcPoint=dt.srcStart, srcPoint<=dt.srcEnd; srcPoint++) {
		float v = pixels[srcPoint];
		for (int i=0; i<dt.kernelSize; i++, tablePointer++)
			pixels2[dt.indices[tablePointer]] += v * dt.weights[tablePointer];
 </code>
 */
public class DownsizeTable {
	/** Number of kernel points per source data point */
	public final int kernelSize;
	/** index of the first point of the source data that should be accessed */
	public final int srcStart;
	/** index of the last point of the source data that should be accessed */
	public final int srcEnd;
	/** For each source point between srcStart and srcEnd, indices of destination
	 *	points where the data should be added.
	 *	Arranged in blocks of 'kernelSize' points. E.g. for kernelSize=2, array
	 *	elements 0,1 are for point srcStart, 2,3 for point srcStart+1, etc. */
	public final int[] indices;
	/** For each source point, weights for adding it to the destination point
	 *	given in the corresponding element of 'indices' */
	public final float[] weights;
	/** Kernel sizes corresponding to the interpolation methods NONE, BILINEAR, BICUBIC */
	private final static int[] kernelSizes = new int[] {1, 2, 4};
	private final int srcOrigin;
    private final double scale;			//source/destination pixel numbers
	private final int interpolationMethod;
	private final static int UNUSED=-1; //marks unused entries in 'indices' array


    // Converts a destination pixel coordinate (index) to the corresponding
	// source coordinate
	// All coordinates refer to the centers of the pixel
	// Also for fractional indices, e.g. dstIndex = i-0.5 for left edge of pixel.
	// The output is also fractional (not converted to int)
	// No check for source array bounds, may result in a value <0 or >= array size.
	private double srcIndex(double dstIndex) {
		return srcOrigin-0.5 + (dstIndex+0.5)*scale;
	}
	// Converts the coordinate (index) of a source pixel to the destination pixel
	private double dstIndex(int srcIndex) {
		return (srcIndex-srcOrigin+0.5)/scale - 0.5;
	}
	// Calculates the kernel value. Only valid within +/- 0.5*kernelSize
	protected float kernel(double x) {
		switch (interpolationMethod) {
			case ImageProcessor.NONE:
				return 1f;
			case ImageProcessor.BILINEAR:
				return 1f - (float)Math.abs(x);
			case ImageProcessor.BICUBIC:
				return (float)ImageProcessor.cubic(x);
		}
		return Float.NaN;
	}

}
