package org.jcarvajal.webapp.utils;

import java.util.Observable;

public class ImmutableObservable extends Observable {
	public ImmutableObservable() {
		setChanged();
	}
	
	@Override
	protected synchronized void clearChanged() {
		
	}
}
