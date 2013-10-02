package de.thm.bi.utils;

/**
 * This interface describes Observer objects that can be registered on
 * Observabale objects. On change of the Observabale objects the update method
 * of the observer is called.
 * 
 */
public interface IObserver {
	void update(Object observable);
}
