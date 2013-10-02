package de.thm.bi.utils;

import java.util.LinkedList;

/**
 * Factory for creating ID Objects
 * 
 * @author Artur Klos
 * 
 */
public class IDFactory {

	private static LinkedList<ID> useableIdNumber = new LinkedList<ID>();

	/**
	 * @return Returns a unique ID that is either recycled or recreated
	 */
	public static final ID createApplicationUniqueId() {
		try {
			if (!useableIdNumber.isEmpty()) {
				return useableIdNumber.removeFirst();
			}
		} catch (Exception e) {
		}
		return new ID();
	}

	/**
	 * @param id
	 *            ID that has to be recycled
	 */
	public static final void recycle(ID id) {
		useableIdNumber.add(id);
	}

}
