package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBPageJsFunction extends DBBase {
	
	private String funcId;
	private String funcName;
	private String jsFileName;
	private String jsFunction;
	private String jsParamlist;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("******page_js_function*********"
                +"FUNC_ID:"+funcId+"\n"
                +"FUNC_NAME:"+funcName+"\n"
                +"JS_FILE_NAME:"+jsFileName+"\n"
                +"JS_FUNCTION:"+jsFunction+"\n"
                +"JS_PARAMLIST:"+jsParamlist+"\n"
                +"EXT1:"+ext1+"\n"
                +"EXT2:"+ext2+"\n"
                +"EXT3:"+ext3+"\n"
                +"EXT4:"+ext4+"\n"
                +"EXT5:"+ext5+"\n"
                +"EXT6:"+ext6+"\n"

            );
		return buf.toString();
	}
	
    public String getFuncId () {
        return this.funcId;
    }
    public String getFuncName () {
        return this.funcName;
    }
    public String getJsFileName () {
        return this.jsFileName;
    }
    public String getJsFunction () {
        return this.jsFunction;
    }
    public String getJsParamlist () {
        return this.jsParamlist;
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


    public void setFuncId (String funcId) {
        this.funcId = funcId;
    }
    public void setFuncName (String funcName) {
        this.funcName = funcName;
    }
    public void setJsFileName (String jsFileName) {
        this.jsFileName = jsFileName;
    }
    public void setJsFunction (String jsFunction) {
        this.jsFunction = jsFunction;
    }
    public void setJsParamlist (String jsParamlist) {
        this.jsParamlist = jsParamlist;
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

