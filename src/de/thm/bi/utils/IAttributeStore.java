package de.thm.bi.utils;

/**
 * Describes a interface for storeing any attribute into a class
 * 
 * @author Artur Klos
 * 
 */
public interface IAttributeStore {
	/**
	 * With this method a stored attribute can be retrieved
	 * 
	 * @param attributeName
	 *            Key
	 * @return Returns the given attribute if a attribute corresponding to the
	 *         attributeName was stored else returns null
	 */
	public Object getStoredAttribute(Object attributeName);

	/**
	 * Checks weather a attribute has been stored
	 * 
	 * @param attributeName
	 *            Attribute name to check
	 * @return Returns true if attribute has been stored false if not
	 */
	public boolean hasStoredAttribute(Object attributeName);

	/**
	 * Stores the attribute object with the key attributeName. With
	 * getStoredAttribute the attribute object can be retrieved.
	 * 
	 * @param attributeName
	 *            Key
	 * @param object
	 *            Value
	 */
	public void storeAttribute(Object attributeName, Object object);
}
