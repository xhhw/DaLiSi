package com.zb.dalisi.service;

import java.util.Map;

public interface SOAEngine {
	public void initEngine();
	public void stopEngine();
	public boolean check(Map sceneProp, Map eventProp, Map eventObjProp);
	
	@Deprecated
	public boolean check(String ruleId);
}
