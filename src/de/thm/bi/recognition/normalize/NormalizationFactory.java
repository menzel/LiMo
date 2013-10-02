package de.thm.bi.recognition.normalize;

/**
 * Factory for creating PCImageAnalyzerStrategies
 * 
 * @author Artur Klos
 * 
 */
public class NormalizationFactory {

	public enum ImageAnalyzerStrategies {
		COLOR_VECTOR, IMAGEJ;
	}

	public static ImageAnalyzerStrategies DEFAULT_STRATEGY = ImageAnalyzerStrategies.IMAGEJ;

	/**
	 * @param strategy
	 *            Strategy to create
	 * @return Returns the specified strategy
	 */
	public static final INormalization create(ImageAnalyzerStrategies strategy) {
		switch (strategy) {
		case COLOR_VECTOR:
			return new ColorVector();
		case IMAGEJ:
			return new ImageJ();
		default:
			return null;
		}
	}

	/**
	 * @return Creates the default Strategy
	 */
	public static final INormalization createDefault() {
		return create(DEFAULT_STRATEGY);
	}
}
