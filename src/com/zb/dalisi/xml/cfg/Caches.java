package com.zb.dalisi.xml.cfg;

import java.util.ArrayList;
import java.util.List;

public class Caches {
	private List list = new ArrayList();
	public void addCache(Cache cache) {
		list.add(cache);
	}
	public Cache[] getCache() {
		return ((Cache[])(Cache[])this.list.toArray(new Cache[0]));
	}
}
