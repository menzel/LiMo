/**
 * 
 */
package de.thm.bi.recognition.patternmatching;

import de.thm.bi.recognition.data.region.IRegion;

/**
 * @author Artur Klos
 * 
 */
public class Size implements IPatternMatcher {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.thm.bi.patternmatching.IPatternMatcher#isMatching(de.thm.bi.segmentation
	 * .data.region.IRegion)
	 */
	@Override
	public boolean isMatching(IRegion region) {
		return false;
	}

}
