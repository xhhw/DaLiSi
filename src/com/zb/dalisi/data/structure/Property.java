package com.zb.dalisi.data.structure;


public class Property implements Comparable<Property> {
	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(Property o) {
		// TODO Auto-generated method stub
		return this.key.compareTo(o.getKey());
	}
}
