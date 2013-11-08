package de.thm.bi.utils;

import java.util.ArrayList;

/**
 * This Class implements the Observable interface.
 * 
 */
public class Observabale implements IObservable {

	private ArrayList<IObserver> observers = new ArrayList<IObservable>();

	@Override
	public void notifyObservers(Object observable) {
		int limit = observers.size();
		for (int i = 0; i < limit; i++) {
			observers.get(i).update(observable);
		}

	}

	@Override
	public void registerObserver(IObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unregisterObserver(IObserver observer) {
		observers.remove(observer);
	}
}
