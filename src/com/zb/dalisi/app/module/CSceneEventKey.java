package com.zb.dalisi.app.module;

public class CSceneEventKey implements Cloneable {
	private String appId;
	private String sceneId;
	private String eventId;
	
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
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj){
			return true;
		}
		if (obj instanceof CSceneEventKey) {
			CSceneEventKey tmp = (CSceneEventKey) obj;
			if (tmp.getSceneId().equals(this.getSceneId())
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
				^ String.valueOf(eventId).hashCode();
		System.out.println(ret);
		return ret;
	}
	@Override
	protected CSceneEventKey clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (CSceneEventKey)this.clone();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sceneId+eventId;
	}
}
