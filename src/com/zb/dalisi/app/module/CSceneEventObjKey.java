package com.zb.dalisi.app.module;

public class CSceneEventObjKey implements Cloneable {
	private String appId;
	private String sceneId;
	private String eventId;
	private String eventObjId;
	
	public CSceneEventObjKey(){
		
	}
	public CSceneEventObjKey(CSceneEventKey sekey){
		this.appId = sekey.getAppId();
		this.sceneId = sekey.getSceneId();
		this.eventId = sekey.getEventId();
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
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj){
			return true;
		}
		if (obj instanceof CSceneEventObjKey) {
			CSceneEventObjKey tmp = (CSceneEventObjKey) obj;
			if (tmp.getEventObjId().equals(this.getEventObjId())
					&& tmp.getSceneId().equals(this.getSceneId())
					&& tmp.getEventId().equals(this.getEventId())) {
				return true;
			}
		}
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int ret = String.valueOf(sceneId).hashCode()
				^ String.valueOf(eventId).hashCode()
				^ String.valueOf(eventObjId).hashCode();
		System.out.println(ret);
		return ret;
	}
	@Override
	protected CSceneEventObjKey clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (CSceneEventObjKey)this.clone();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sceneId+eventId+eventObjId;
	}
}
