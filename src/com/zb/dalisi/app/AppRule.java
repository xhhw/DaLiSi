package com.zb.dalisi.app;

import com.zb.dalisi.app.module.CDefRule;
import com.zb.dalisi.app.module.CSceneEventKey;
import com.zb.dalisi.app.module.CSceneEventObjKey;
import com.zb.dalisi.expression.Expression;
import com.zb.dalisi.expression.ExpressionFactory;

public class AppRule implements RunableRule {
	private CDefRule rule;
	private Observable observable;
	private boolean result = false;
	private String appId;
	private String sceneId;
	private String eventId;
	private String eventObjId;
	
	public AppRule(CDefRule rule, CSceneEventKey key) {
		this.rule = rule;
		this.appId = key.getAppId();
		this.sceneId = key.getSceneId();
		this.eventId = key.getEventId();
		observable = new Observable(this);
	}

	public AppRule(CDefRule rule, CSceneEventObjKey key) {
		this.rule = rule;
		this.appId = key.getAppId();
		this.sceneId = key.getSceneId();
		this.eventId = key.getEventId();
		this.eventObjId = key.getEventObjId();
		observable = new Observable(this);
	}
	
	public AppRule(CDefRule rule, AppRule key) {
		this.rule = rule;
		this.appId = key.getAppId();
		this.sceneId = key.getSceneId();
		this.eventId = key.getEventId();
		this.eventObjId = key.getEventObjId();
		observable = new Observable(this);
	}

	public CDefRule getRule() {
		return rule;
	}

	public void setRule(CDefRule rule) {
		this.rule = rule;
	}

	public void registerObserver(Observer observer) {
		// TODO Auto-generated method stub
		observable.registerObserver(observer);
	}

	public void notifyObservers() {
		// TODO Auto-generated method stub
		observable.notifyObservers();
	}

	public boolean run() {
		// TODO Auto-generated method stub
		String strExpression = rule.getExpression();
		System.out.println(strExpression);
		
		Expression expression = ExpressionFactory.getInstance().getExpression(strExpression);
		result = expression.evaluate().getBooleanValue();
		
		notifyObservers();
		return result;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventObjId() {
		return eventObjId;
	}

	public void setEventObjId(String eventObjId) {
		this.eventObjId = eventObjId;
	}
}
