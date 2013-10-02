package de.thm.bi.utils;

/**
 * This interface describes the operation of a object that is observable by
 * observer.
 * 
 */
public interface IObservable {
	/**
	 * Benachrichtigt alle Observer
	 */
	public void notifyObservers(Object observable);

	/**
	 * Registiert einen Observer beim Observabale
	 * 
	 * @param observer
	 *            Der Observer der das Observabale observieren m??chte
	 */
	public void registerObserver(IObserver observer);

	/**
	 * L??scht die Registration beim Observabale
	 * 
	 * @param observer
	 *            Der zu entfernende Observer
	 */
	public void unregisterObserver(IObserver observer);
}
