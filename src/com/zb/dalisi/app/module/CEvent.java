package com.zb.dalisi.app.module;


public class CEvent extends CCombEntity {

	public CEvent(String eventId){
		super(eventId);
	}
	
	public String getEventId(){
		return super.getId();
	}

}
