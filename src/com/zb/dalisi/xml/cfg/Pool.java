package com.zb.dalisi.xml.cfg;

import java.util.ArrayList;
import java.util.List;

public class Pool {
	private String name = null;
	private String primary = null;
	private List list = new ArrayList();
	public void addProperty(Property property) {
		list.add(property);
	}
	public Property[] getProperty() {
		return ((Property[])(Property[])this.list.toArray(new Property[0]));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}

}
