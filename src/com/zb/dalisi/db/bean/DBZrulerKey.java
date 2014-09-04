package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBZrulerKey extends DBBase {
	
	private String keyId;
	private String keySymbol;
	private String keyType;
	private String driverIntfType;
	private String driverClass;
	private String driverMethod;
	private String driverParamlist;
	private String driverReturn;
	private String keyDesc;
	private String parentKey;
	private String status;
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
		buf.append("******zruler_key*********"
                +"KEY_ID:"+keyId+"\n"
                +"KEY_SYMBOL:"+keySymbol+"\n"
                +"KEY_TYPE:"+keyType+"\n"
                +"DRIVER_INTF_TYPE:"+driverIntfType+"\n"
                +"DRIVER_CLASS:"+driverClass+"\n"
                +"DRIVER_METHOD:"+driverMethod+"\n"
                +"DRIVER_PARAMLIST:"+driverParamlist+"\n"
                +"DRIVER_RETURN:"+driverReturn+"\n"
                +"KEY_DESC:"+keyDesc+"\n"
                +"PARENT_KEY:"+parentKey+"\n"
                +"STATUS:"+status+"\n"
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
	
    public String getKeyId () {
        return this.keyId;
    }
    public String getKeySymbol () {
        return this.keySymbol;
    }
    public String getKeyType () {
        return this.keyType;
    }
    public String getDriverIntfType () {
        return this.driverIntfType;
    }
    public String getDriverClass () {
        return this.driverClass;
    }
    public String getDriverMethod () {
        return this.driverMethod;
    }
    public String getDriverParamlist () {
        return this.driverParamlist;
    }
    public String getDriverReturn () {
        return this.driverReturn;
    }
    public String getKeyDesc () {
        return this.keyDesc;
    }
    public String getParentKey () {
        return this.parentKey;
    }
    public String getStatus () {
        return this.status;
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


    public void setKeyId (String keyId) {
        this.keyId = keyId;
    }
    public void setKeySymbol (String keySymbol) {
        this.keySymbol = keySymbol;
    }
    public void setKeyType (String keyType) {
        this.keyType = keyType;
    }
    public void setDriverIntfType (String driverIntfType) {
        this.driverIntfType = driverIntfType;
    }
    public void setDriverClass (String driverClass) {
        this.driverClass = driverClass;
    }
    public void setDriverMethod (String driverMethod) {
        this.driverMethod = driverMethod;
    }
    public void setDriverParamlist (String driverParamlist) {
        this.driverParamlist = driverParamlist;
    }
    public void setDriverReturn (String driverReturn) {
        this.driverReturn = driverReturn;
    }
    public void setKeyDesc (String keyDesc) {
        this.keyDesc = keyDesc;
    }
    public void setParentKey (String parentKey) {
        this.parentKey = parentKey;
    }
    public void setStatus (String status) {
        this.status = status;
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

