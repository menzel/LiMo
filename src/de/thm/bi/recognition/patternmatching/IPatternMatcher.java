/**
 * 
 */
package de.thm.bi.recognition.patternmatching;

import de.thm.bi.recognition.data.region.IRegion;

/**
 * @author Artur Klos
 * 
 */
public interface IPatternMatcher {
	/**
	 * 
	 * 
	 * @param region
	 * @return
	 */
	public boolean isMatching(IRegion region);
}
