package de.thm.bi.utils;

/**
 * This class generates a unique id for each instance it can be used to identify
 * objects
 * 
 * @author Artur Klos
 * 
 */
public class ID {

	private static long idCounter = 0;
	private long id;

	public ID() {
		synchronized (ID.class) {
			this.id = idCounter++;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		IDFactory.recycle(this);
	}

	/**
	 * @return Returns a unique id
	 */
	public long getId() {
		return id;
	}
}
