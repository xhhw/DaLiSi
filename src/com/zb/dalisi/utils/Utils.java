package com.zb.dalisi.utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Utils {
	public static Map sortMap(Map map) {
		Map<Object, Object> mapVK = new TreeMap<Object, Object>(
				new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						String v1 = (String) obj1;
						String v2 = (String) obj2;
						int s = v2.compareTo(v1);
						return s;
					}
				});

		Set col = map.keySet();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object value = map.get(key);
			mapVK.put(key, value);
		}
		return mapVK;
	}

	public static String decodeColName(String src){
		//引擎所用JAVA对象为ColName解码转换成数据库表名为col_name
		StringBuffer buf = new StringBuffer();
		char[] c = src.toCharArray();
		for (int i=0; i<c.length; i++){
			if(i>0 && Character.isUpperCase(c[i])){
				buf.append('_');
			}
			buf.append(Character.toLowerCase(c[i]));
		}
		return buf.toString();
	}
	
}
