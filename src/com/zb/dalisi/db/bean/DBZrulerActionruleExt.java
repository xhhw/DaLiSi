package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBZrulerActionruleExt extends DBBase {
	
	private String appId;
	private String sceneId;
	private String eventId;
	private String attrId;
	private String attrValue;
	private String status;
	private String appDesc;
	private String remark;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	private String ext7;
	private String ext8;

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("******ZRULER_ACTIONRULE_EXT*********"
                +"APP_ID:"+appId+"\n"
                +"SCENE_ID:"+sceneId+"\n"
                +"EVENT_ID:"+eventId+"\n"
                +"ATTR_ID:"+attrId+"\n"
                +"ATTR_VALUE:"+attrValue+"\n"
                +"STATUS:"+status+"\n"
                +"APP_DESC:"+appDesc+"\n"
                +"REMARK:"+remark+"\n"
                +"EXT1:"+ext1+"\n"
                +"EXT2:"+ext2+"\n"
                +"EXT3:"+ext3+"\n"
                +"EXT4:"+ext4+"\n"
                +"EXT5:"+ext5+"\n"
                +"EXT6:"+ext6+"\n"
                +"EXT7:"+ext7+"\n"
                +"EXT8:"+ext8+"\n"

            );
		return buf.toString();
	}
	
    public String getAppId () {
        return this.appId;
    }
    public String getSceneId () {
        return this.sceneId;
    }
    public String getEventId () {
        return this.eventId;
    }
    public String getAttrId () {
        return this.attrId;
    }
    public String getAttrValue () {
        return this.attrValue;
    }
    public String getStatus () {
        return this.status;
    }
    public String getAppDesc () {
        return this.appDesc;
    }
    public String getRemark () {
        return this.remark;
    }
    public String getExt1 () {
        return this.ext1;
    }
    public String getExt2 () {
        return this.ext2;
    }
    public String getExt3 () {
        return this.ext3;
    }
    public String getExt4 () {
        return this.ext4;
    }
    public String getExt5 () {
        return this.ext5;
    }
    public String getExt6 () {
        return this.ext6;
    }
    public String getExt7 () {
        return this.ext7;
    }
    public String getExt8 () {
        return this.ext8;
    }


    public void setAppId (String appId) {
        this.appId = appId;
    }
    public void setSceneId (String sceneId) {
        this.sceneId = sceneId;
    }
    public void setEventId (String eventId) {
        this.eventId = eventId;
    }
    public void setAttrId (String attrId) {
        this.attrId = attrId;
    }
    public void setAttrValue (String attrValue) {
        this.attrValue = attrValue;
    }
    public void setStatus (String status) {
        this.status = status;
    }
    public void setAppDesc (String appDesc) {
        this.appDesc = appDesc;
    }
    public void setRemark (String remark) {
        this.remark = remark;
    }
    public void setExt1 (String ext1) {
        this.ext1 = ext1;
    }
    public void setExt2 (String ext2) {
        this.ext2 = ext2;
    }
    public void setExt3 (String ext3) {
        this.ext3 = ext3;
    }
    public void setExt4 (String ext4) {
        this.ext4 = ext4;
    }
    public void setExt5 (String ext5) {
        this.ext5 = ext5;
    }
    public void setExt6 (String ext6) {
        this.ext6 = ext6;
    }
    public void setExt7 (String ext7) {
        this.ext7 = ext7;
    }
    public void setExt8 (String ext8) {
        this.ext8 = ext8;
    }


}

