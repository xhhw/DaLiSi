package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBZrulerAttribute extends DBBase {
	
	private String attrId;
	private String attrType;
	private String attrEnsName;
	private String attrChsName;
	private String displayType;
	private String dataType;
	private String displayName;
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
		buf.append("******zruler_attribute*********"
                +"ATTR_ID:"+attrId+"\n"
                +"ATTR_TYPE:"+attrType+"\n"
                +"ATTR_ENS_NAME:"+attrEnsName+"\n"
                +"ATTR_CHS_NAME:"+attrChsName+"\n"
                +"DISPLAY_TYPE:"+displayType+"\n"
                +"DATA_TYPE:"+dataType+"\n"
                +"DISPLAY_NAME:"+displayName+"\n"
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
	
    public String getAttrId () {
        return this.attrId;
    }
    public String getAttrType () {
        return this.attrType;
    }
    public String getAttrEnsName () {
        return this.attrEnsName;
    }
    public String getAttrChsName () {
        return this.attrChsName;
    }
    public String getDisplayType () {
        return this.displayType;
    }
    public String getDataType () {
        return this.dataType;
    }
    public String getDisplayName () {
        return this.displayName;
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


    public void setAttrId (String attrId) {
        this.attrId = attrId;
    }
    public void setAttrType (String attrType) {
        this.attrType = attrType;
    }
    public void setAttrEnsName (String attrEnsName) {
        this.attrEnsName = attrEnsName;
    }
    public void setAttrChsName (String attrChsName) {
        this.attrChsName = attrChsName;
    }
    public void setDisplayType (String displayType) {
        this.displayType = displayType;
    }
    public void setDataType (String dataType) {
        this.dataType = dataType;
    }
    public void setDisplayName (String displayName) {
        this.displayName = displayName;
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

