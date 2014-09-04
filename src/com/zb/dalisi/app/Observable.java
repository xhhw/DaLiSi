package com.zb.dalisi.app;

import java.util.ArrayList;

public class Observable implements ResultObservable {
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	ResultObservable rule;
	
	public Observable(ResultObservable rule){
		this.rule = rule;
	}
	
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers() {
//		Iterator iterator = observers.iterator();
//		while (iterator.hasNext()){
//			Observer observer = (Observer)iterator.next();
//			observer.getReslut(rule);
//		}
		
		for (Observer observer : observers) {
			observer.getReslut(rule);
        }
		
	}

}
