package com.zb.dalisi.app.module;


public class CEventObject extends CCombEntity {
	
	public CEventObject(String eventObjectId){
		super(eventObjectId);
	}
	
	public String getEventObjectId(){
		return super.getId();
	}
}
