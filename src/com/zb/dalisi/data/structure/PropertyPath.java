package com.zb.dalisi.data.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PropertyPath {
	private static PropertyPathNode propertyPathRoot = new PropertyPathNode(0);
	private static PropertyPath instance = new PropertyPath();
	private PropertyPath(){
	}
	public PropertyPath getInstance(){
		return this.instance;
	}
	
	public void addPropertyPath(PropertyComposite object){
		ArrayList<Property> list = object.getPropertys();
		Collections.sort(list);
		PropertyPathNode node = this.propertyPathRoot;
		PropertyPathNode next = null;
		int length = list.size();
		for(int i=0;i<length;i++){
			Property prop = list.get(i);
			if(node.getLevel()==(length-1)){
				next = node.addProperty(prop.getKey(), prop.getValue(), object);
			}else{
				next = node.addProperty(prop.getKey(), prop.getValue());
			}
			node = next;
		}
	}
	
	public PropertyComposite getPropertyComposite(HashMap map){
		ArrayList<Property> list = new ArrayList<Property>();
		Set sets = map.keySet();
		for (Iterator iter = sets.iterator(); iter.hasNext(); ) {
			String key = (String)iter.next();
			String value = (String)map.get(key);
			Property prop = new Property();
			prop.setKey(key);
			prop.setValue(value);
			list.add(prop);
		}
		return getPropertyComposite(list);
	}
	
	public PropertyComposite getPropertyComposite(ArrayList<Property> list){
		PropertyComposite rtn = null;
		Collections.sort(list);
		PropertyPathNode node = this.propertyPathRoot;
		PropertyPathNode next = null;
		int length = list.size();
		for(int i=0;i<list.size();i++){
			Property prop = list.get(i);
			if (!node.containsProperty(prop.getKey(), prop.getValue())){
				break;
			}
			if (node.getLevel()==(length-1)){
				rtn = node.getPropertyComposite(prop.getKey(), prop.getValue());
			}else{
				next = node.next(prop.getKey(), prop.getValue());
				if(next == null){
					break;
				}
			}
			node = next;
		}
		return rtn;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
