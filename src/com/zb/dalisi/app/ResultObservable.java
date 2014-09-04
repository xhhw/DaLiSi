package com.zb.dalisi.app;

public interface ResultObservable {
	public void registerObserver(Observer observer);
	public void notifyObservers();
}
