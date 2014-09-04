package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBPageApp extends DBBase {
	
	private String pageUrl;
	private String elementId;
	private String event;
	private String funcId;
	private String pageType;
	private String pageParent;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("******page_app*********"
                +"PAGE_URL:"+pageUrl+"\n"
                +"ELEMENT_ID:"+elementId+"\n"
                +"EVENT:"+event+"\n"
                +"FUNC_ID:"+funcId+"\n"
                +"PAGE_TYPE:"+pageType+"\n"
                +"PAGE_PARENT:"+pageParent+"\n"
                +"EXT1:"+ext1+"\n"
                +"EXT2:"+ext2+"\n"
                +"EXT3:"+ext3+"\n"
                +"EXT4:"+ext4+"\n"
                +"EXT5:"+ext5+"\n"
                +"EXT6:"+ext6+"\n"

            );
		return buf.toString();
	}
	
    public String getPageUrl () {
        return this.pageUrl;
    }
    public String getElementId () {
        return this.elementId;
    }
    public String getEvent () {
        return this.event;
    }
    public String getFuncId () {
        return this.funcId;
    }
    public String getPageType () {
        return this.pageType;
    }
    public String getPageParent () {
        return this.pageParent;
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


    public void setPageUrl (String pageUrl) {
        this.pageUrl = pageUrl;
    }
    public void setElementId (String elementId) {
        this.elementId = elementId;
    }
    public void setEvent (String event) {
        this.event = event;
    }
    public void setFuncId (String funcId) {
        this.funcId = funcId;
    }
    public void setPageType (String pageType) {
        this.pageType = pageType;
    }
    public void setPageParent (String pageParent) {
        this.pageParent = pageParent;
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


}

