package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DBZrulerRule extends DBBase {
	
	private String ruleId;
	private String ruleName;
	private String ruleAttentionType;
	private String ruleType;
	private String driverType;
	private String expression;
	private String ruleDesc;
	private String msgCode;
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
		buf.append("******zruler_rule*********"
                +"RULE_ID:"+ruleId+"\n"
                +"RULE_NAME:"+ruleName+"\n"
                +"RULE_ATTENTION_TYPE:"+ruleAttentionType+"\n"
                +"RULE_TYPE:"+ruleType+"\n"
                +"DRIVER_TYPE:"+driverType+"\n"
                +"EXPRESSION:"+expression+"\n"
                +"RULE_DESC:"+ruleDesc+"\n"
                +"MSG_CODE:"+msgCode+"\n"
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
	
    public String getRuleId () {
        return this.ruleId;
    }
    public String getRuleName () {
        return this.ruleName;
    }
    public String getRuleAttentionType () {
        return this.ruleAttentionType;
    }
    public String getRuleType () {
        return this.ruleType;
    }
    public String getDriverType () {
        return this.driverType;
    }
    public String getExpression () {
        return this.expression;
    }
    public String getRuleDesc () {
        return this.ruleDesc;
    }
    public String getMsgCode () {
        return this.msgCode;
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


    public void setRuleId (String ruleId) {
        this.ruleId = ruleId;
    }
    public void setRuleName (String ruleName) {
        this.ruleName = ruleName;
    }
    public void setRuleAttentionType (String ruleAttentionType) {
        this.ruleAttentionType = ruleAttentionType;
    }
    public void setRuleType (String ruleType) {
        this.ruleType = ruleType;
    }
    public void setDriverType (String driverType) {
        this.driverType = driverType;
    }
    public void setExpression (String expression) {
        this.expression = expression;
    }
    public void setRuleDesc (String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }
    public void setMsgCode (String msgCode) {
        this.msgCode = msgCode;
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

