package com.zb.dalisi.app.module;


public class CScene extends CCombEntity {
	
	public CScene(String sceneId){
		super(sceneId);
	}
	
	public String getSceneId(){
		return super.getId();
	}
	
}
