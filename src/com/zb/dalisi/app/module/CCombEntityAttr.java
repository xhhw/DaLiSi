package com.zb.dalisi.app.module;

import com.zb.dalisi.cache.CacheAttr;
import com.zb.dalisi.frame.cache.CacheHelper;


public class CCombEntityAttr extends CLogicBase {
	private String attrId;
	private Object attrValue;
	private String status;
	
	public CCombEntityAttr(){
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = ((CacheAttr)CacheHelper.getInstance().getCache(CacheAttr.class)).getCDefAttr(attrId).getDataFormString(attrValue);
	}
	public String getAttrId() {
		return attrId;
	}
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("##classname : "+CCombEntityAttr.class.getName()+"\n");
		buf.append("   attrId : "+attrId+"\n");
		buf.append("attrValue : "+attrValue+"\n");
		buf.append("   status : "+status+"\n");
		return buf.toString();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.attrId.hashCode()^this.attrValue.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null){
			return false;
		}else if (obj == this){
			return true;
		}else if (obj instanceof CCombEntityAttr){
			return this.attrId.equals(((CCombEntityAttr) obj).getAttrId())&&this.attrValue.equals(((CCombEntityAttr) obj).getAttrValue());
		}
		return super.equals(obj);
	}
}
