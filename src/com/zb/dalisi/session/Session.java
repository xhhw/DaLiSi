package com.zb.dalisi.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zb.dalisi.app.AppResult;
import com.zb.dalisi.app.AppRule;
import com.zb.dalisi.app.module.CDefKey;
import com.zb.dalisi.app.module.CEvent;
import com.zb.dalisi.app.module.CEventObject;
import com.zb.dalisi.app.module.CScene;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.cache.CacheKey;
import com.zb.dalisi.expression.syntax.function.Function;
import com.zb.dalisi.expression.syntax.function.UserFunction;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.Valuable;

public class Session {
	private String appId;
	private CScene scene;
	private CEvent event;
	private ArrayList<CEventObject> eventObjects = new ArrayList<CEventObject>();
	private AppResult appResult;
	private AppRule curAppRule;
	
	private final Map<String, Object> key_values = new HashMap<String, Object>();
	
	public Session(){}

	public Object getValue(String key){
		Object value = null;
		if (key_values.containsKey(key)){
			value = key_values.get(key);
		}
		else{
			value = findParentKey(key);
			if (value == null){
				throw new RuleException("本session中参数"+key+"未定义.");
			}
		}
		return value;
	}
	public Session putValue(String key, Object value){
		key_values.put(key, value);
		return this;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public CScene getScene() {
		return scene;
	}
	public void setScene(CScene scene) {
		this.scene = scene;
	}

	public CEvent getEvent() {
		return event;
	}

	public void setEvent(CEvent event) {
		this.event = event;
	}

	public ArrayList<CEventObject> getEventObjects() {
		return eventObjects;
	}

	public void setEventObjects(ArrayList<CEventObject> eventObjects) {
		this.eventObjects = eventObjects;
	}

	public AppResult getAppResult() {
		return appResult;
	}

	public void setAppResult(AppResult appResult) {
		this.appResult = appResult;
	}
	
	protected Object findParentKey(String key) {
		CDefKey dependsKey = CacheKey.getCDefKey(key);
		Object dependsValue = getValue(dependsKey.getDependsKey());
		Object result = null;
		try {
			if ("08".equals(dependsKey.getDriverIntfType())){
				UserFunction func = new UserFunction(dependsKey.getDriverClass().newInstance(),dependsKey.getDriverMethod());
				Valuable[] args = new Valuable[func.getArgumentNum()];
				for(int i=func.getArgumentNum()-1; i>=0; i--) {
					args[i] = TokenBuilder.buildRuntimeValue(dependsValue);
				}
				result = func.execute(args);
			}
			else{
				Function func = (Function) dependsKey.getDriverClass().newInstance();
				Valuable[] args = new Valuable[func.getArgumentNum()];
				for(int i=func.getArgumentNum()-1; i>=0; i--) {
					args[i] = TokenBuilder.buildRuntimeValue(dependsValue);
				}
				result = func.execute(args);
			}
			putValue(key, result);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	public AppRule getCurAppRule() {
		return curAppRule;
	}

	public void setCurAppRule(AppRule curAppRule) {
		this.curAppRule = curAppRule;
	}
}
