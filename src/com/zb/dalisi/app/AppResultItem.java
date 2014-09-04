package com.zb.dalisi.app;


public class AppResultItem {
	private String id;
	private boolean result;
	private String code;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isPassed() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("##rule(" + getId() + ") just run.\n");
		buf.append("   result : " + isPassed() + "\n");
		buf.append("  msgcode : " + getCode() + "\n");
		return buf.toString();
	}
}
