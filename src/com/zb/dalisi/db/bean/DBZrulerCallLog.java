package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBZrulerCallLog extends DBBase {
	
	private String appId;
	private String appArgs;
	private String msgCode;
	private int result;
	private java.util.Date doneDate;

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("******ZRULER_CALL_LOG*********"
                +"APP_ID:"+appId+"\n"
                +"APP_ARGS:"+appArgs+"\n"
                +"MSG_CODE:"+msgCode+"\n"
                +"RESULT:"+result+"\n"
                +"DONE_DATE:"+doneDate+"\n"

            );
		return buf.toString();
	}
	
    public String getAppId () {
        return this.appId;
    }
    public String getAppArgs () {
        return this.appArgs;
    }
    public String getMsgCode () {
        return this.msgCode;
    }
    public int getResult () {
        return this.result;
    }
    public java.util.Date getDoneDate () {
        return this.doneDate;
    }


    public void setAppId (String appId) {
        this.appId = appId;
    }
    public void setAppArgs (String appArgs) {
        this.appArgs = appArgs;
    }
    public void setMsgCode (String msgCode) {
        this.msgCode = msgCode;
    }
    public void setResult (int result) {
        this.result = result;
    }
    public void setDoneDate (java.util.Date doneDate) {
        this.doneDate = doneDate;
    }


}

