package com.zb.dalisi.app;

import java.util.List;

import com.zb.dalisi.app.module.CDefRule;

public class AppRuleNode implements RunableRule {
	private CDefRule rule;
	private Observable observable;
	private boolean result = false;
	private String appId;
	private String sceneId;
	private String eventId;
	private String eventObjId;
	private List<AppRuleNode> nextNodes;

	@Override
	public void registerObserver(Observer observer) {
		// TODO Auto-generated method stub
		observable.registerObserver(observer);
	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		observable.notifyObservers();
	}

	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		notifyObservers();
		return false;
	}
	
	public void testABC(){
		getClass().getClassLoader().getResource("");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
