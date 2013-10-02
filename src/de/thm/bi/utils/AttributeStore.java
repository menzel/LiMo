/**
 * 
 */
package de.thm.bi.utils;

import java.util.HashMap;

/**
 * @author Artur Klos
 * 
 */
public class AttributeStore implements IAttributeStore {

	private HashMap<String, Object> attributeStore = new HashMap<String, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#getStoredAttribute(java.lang.Object
	 * )
	 */
	@Override
	public Object getStoredAttribute(Object attributeName) {
		return attributeStore.get(attributeName.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#hasStoredAttribute(java.lang.Object
	 * )
	 */
	@Override
	public boolean hasStoredAttribute(Object attributeName) {
		return attributeStore.get(attributeName) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbpcanalyzer.ak.utils.IAttributeStore#storeAttribute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void storeAttribute(Object attributeName, Object object) {
		attributeStore.put(attributeName.toString(), object);

	}

}
