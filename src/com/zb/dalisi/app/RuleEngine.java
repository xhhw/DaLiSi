package com.zb.dalisi.app;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CDefRule;
import com.zb.dalisi.app.module.CEvent;
import com.zb.dalisi.app.module.CEventObject;
import com.zb.dalisi.app.module.CScene;
import com.zb.dalisi.app.module.CSceneEventKey;
import com.zb.dalisi.app.module.CSceneEventObjKey;
import com.zb.dalisi.app.module.CSceneEventObjRule;
import com.zb.dalisi.app.module.CSceneEventRule;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.cache.CacheEvent;
import com.zb.dalisi.cache.CacheEventObject;
import com.zb.dalisi.cache.CacheRule;
import com.zb.dalisi.cache.CacheScene;
import com.zb.dalisi.cache.CacheSceneEventObjectRules;
import com.zb.dalisi.cache.CacheSceneEventRules;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerCallLog;
import com.zb.dalisi.session.Session;
import com.zb.dalisi.session.SessionManager;

public class RuleEngine {
	private transient static Logger log = Logger.getLogger(RuleEngine.class); 
	
	//输入参数为三个MAP，分别对应场景、事件、驱动对象的属性清单
	//引擎根据属性清单查找配置，获取场景（或事件、驱动对象）对象
	private Map<String, String> sceneMap = new HashMap<String, String>();
	private Map<String, String> eventMap = new HashMap<String, String>();
	private ArrayList<Map<String, String>> eventObjList = new ArrayList<Map<String, String>>();
	private Map<String, Object> args = new HashMap<String, Object>();
	
	private String appId;
	private CScene scene;
	private CEvent event;
	private ArrayList<CEventObject> objs;
	
	private ArrayList<AppRule> appRules = new ArrayList<AppRule>();
	private AppResult appResult = new AppResult();
	
	private boolean preCheck = false;
	
	private RuleEngine(){
	}
	public static RuleEngine getEngine(){
		return new RuleEngine();
	}
	
	public void prepareUserData() {
		scene = CacheScene.findScene(sceneMap);
		if (scene == null) {
			throw new RuleException("找不到对应的场景!");
		}
		event = CacheEvent.findEvent(eventMap);
		if (event == null) {
			throw new RuleException("找不到对应的事件!");
		}
		objs = CacheEventObject.findObjects(eventObjList);
	}
	
	public void putSceneAttr(String name, String value){
		this.sceneMap.put(name, value);
	}
	public void putEventAttr(String name, String value){
		this.eventMap.put(name, value);
	}
	public void putEventObjAttr(Map<String,String> value){
		this.eventObjList.add(value);
	}
	public void putArgs(String string, Object value) {
		this.args.put(string, value);
	}
	public void putArgs(Map map) {
		this.args.putAll(map);
	}
	
	public boolean run(){
		log.debug("run begin");
		prepareUserData();
		Session session = SessionManager.getSession();
		session.setScene(scene);
		session.setEvent(event);
		session.setEventObjects(objs);
		session.setAppResult(appResult);
		
		Set<Map.Entry<String, Object>> sett = args.entrySet();
		for (Iterator<Map.Entry<String, Object>> it = sett.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
			session.putValue(entry.getKey(), entry.getValue());
		}
		
		CSceneEventKey keyEvent = new CSceneEventKey();
		keyEvent.setSceneId(scene.getSceneId());
		keyEvent.setEventId(event.getEventId());
		ArrayList<CSceneEventRule> listEventRules = CacheSceneEventRules.getSceneEventRules(keyEvent);
		for (int i=0;i<listEventRules.size();i++){
			CSceneEventRule sceneEventRule = listEventRules.get(i);
			CDefRule rule = CacheRule.getCDefRule(sceneEventRule.getRuleId());
			AppRule appRule = new AppRule(rule,sceneEventRule.getKey());
			appRules.add(appRule);
		}
		
		CSceneEventObjKey keyEventObj = new CSceneEventObjKey(keyEvent);
		for (int i=0;i<objs.size();i++){
			keyEventObj.setEventObjId(objs.get(i).getEventObjectId());
			ArrayList<CSceneEventObjRule> listEventObjRules = CacheSceneEventObjectRules.getSceneEventRules(keyEventObj);
			for (int j=0;j<listEventObjRules.size();j++){
				CSceneEventObjRule sceneEventObjRule = listEventObjRules.get(j);
				log.debug(sceneEventObjRule.toString());
				CDefRule rule = CacheRule.getCDefRule(sceneEventObjRule.getRuleId());
				AppRule appRule = new AppRule(rule,sceneEventObjRule.getKey());
				appRules.add(appRule);
			}
		}
		
		boolean ret = false;
		try {
			for (int i = 0; i < appRules.size(); i++) {
				appRules.get(i).registerObserver(appResult);
				this.appId = appRules.get(i).getAppId();
				SessionManager.getSession().setCurAppRule(appRules.get(i));
				ret = appRules.get(i).run();
				if (!ret && preCheck) {
					log.debug("预判模式继续执行");
				} else if (!ret) {
					log.debug("闯关模式终端执行");
					break;
				}
			}
		} catch (RuleException e) {
			ret = false;
			e.printStackTrace();
			log.debug(e.getMessage());
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
			log.debug(e.getMessage());
		}
		
//		Iterator iterator = appRules.iterator();
//		while (iterator.hasNext()){
//			RunableRule runable = (RunableRule)iterator.next();
//			ret = runable.run();
//			if (!ret && true){
//				break;
//			}
//		}
		DBZrulerCallLog rec = new DBZrulerCallLog();
		rec.setAppId(this.appId);
		rec.setAppArgs(this.sceneMap.toString());
		rec.setMsgCode("");
		rec.setResult(new BigDecimal(ret?0:1).intValue());
		rec.setDoneDate(new Timestamp((new Date()).getTime()));
		DBEngine.instance(rec.getClass()).insert(rec);
		this.getAppResult().loggingDB(rec);
		
		SessionManager.endSession();
		return ret;
	}
	
	public AppResult getAppResult() {
		return appResult;
	}
	public void setAppResult(AppResult appResult) {
		this.appResult = appResult;
	}
	public boolean isPreCheck() {
		return preCheck;
	}
	public void setPreCheck(boolean preCheck) {
		this.preCheck = preCheck;
	}
	
}
