package ij.measure;

public interface Measurements {
	int AREA=1;
    int MEAN=2;
    int STD_DEV=4;
    int MODE=8;
    int MIN_MAX=16;
    int CENTROID=32;
    int CENTER_OF_MASS=64;
    int PERIMETER=128;
    int LIMIT = 256;
    int RECT=512;
    int LABELS=1024;
    int ELLIPSE=2048;
    int INVERT_Y=4096;
    int SHAPE_DESCRIPTORS=8192;
    int FERET=16384;
    int INTEGRATED_DENSITY=0x8000;
    int MEDIAN=0x10000;
    int SKEWNESS=0x20000;
    int KURTOSIS=0x40000;
    int AREA_FRACTION=0x80000;
    int SLICE=0x100000;
    int STACK_POSITION=0x100000;
    int SCIENTIFIC_NOTATION=0x200000;
    int ADD_TO_OVERLAY=0x400000;
		
	/** Maximum number of calibration standard (20) */
    int MAX_STANDARDS = 20;

}
