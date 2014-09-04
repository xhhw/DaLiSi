package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBZrulerMsgcode extends DBBase {
	
	private String msgCode;
	private String msgDesc;
	private String remark;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("******zruler_msgcode*********"
                +"MSG_CODE:"+msgCode+"\n"
                +"MSG_DESC:"+msgDesc+"\n"
                +"REMARK:"+remark+"\n"
                +"EXT1:"+ext1+"\n"
                +"EXT2:"+ext2+"\n"
                +"EXT3:"+ext3+"\n"
                +"EXT4:"+ext4+"\n"

            );
		return buf.toString();
	}
	
    public String getMsgCode () {
        return this.msgCode;
    }
    public String getMsgDesc () {
        return this.msgDesc;
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


    public void setMsgCode (String msgCode) {
        this.msgCode = msgCode;
    }
    public void setMsgDesc (String msgDesc) {
        this.msgDesc = msgDesc;
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


}

