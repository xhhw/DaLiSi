package com.zb.dalisi.data.structure;

import java.util.HashMap;

import com.zb.dalisi.data.structure.AVLTree.Entry;

public class PropertyPathNode {
	private int level = 0;
	private HashMap<String, AVLTree<ValueWithNext> > decisionPolicies = new HashMap<String, AVLTree<ValueWithNext> >();
	
	public PropertyPathNode(int level){
		this.level = level;
	}
	
	public int getLevel(){
		return this.level;
	}
	
	public boolean containsProperty(String key, String value){
		boolean rtn = false;
		if(this.decisionPolicies.containsKey(key)){
			AVLTree<ValueWithNext> tree = this.decisionPolicies.get(key);
			Entry<ValueWithNext> element = new Entry<ValueWithNext>();
			ValueWithNext valueWithNext = new ValueWithNext(value);
			element.element = valueWithNext;
			if(tree.containsKey(element)){
				rtn = true;
			}
		}
		return rtn;
	}
	
	public PropertyPathNode next(String key, String value){
		PropertyPathNode next = null;
		if(this.decisionPolicies.containsKey(key)){
			AVLTree<ValueWithNext> tree = this.decisionPolicies.get(key);
			Entry<ValueWithNext> element = new Entry<ValueWithNext>();
			ValueWithNext valueWithNext = new ValueWithNext(value);
			element.element = valueWithNext;
			if(tree.containsKey(element)){
				next = tree.get(element).getNext();
			}
		}
		return next;
	}
	
	public PropertyPathNode addProperty(String key, String value){
		ValueWithNext valueWithNext = new ValueWithNext(value);
		valueWithNext.setNext(new PropertyPathNode(this.level+1));
		Entry<ValueWithNext> element = new Entry<ValueWithNext>();
		element.element = valueWithNext;
		if (this.decisionPolicies.containsKey(key)) {
			AVLTree<ValueWithNext> tree = this.decisionPolicies.get(key);
			if (!tree.containsKey(element)) {
				tree.add(valueWithNext);
			}
		} else {
			AVLTree<ValueWithNext> tree = new AVLTree<ValueWithNext>();
			tree.add(valueWithNext);
			this.decisionPolicies.put(key, tree);
		}
		return valueWithNext.getNext();
	}
	
	public PropertyPathNode addProperty(String key, String value, PropertyComposite object){
		Entry<ValueWithNext> element = new Entry<ValueWithNext>();
		ValueWithNext valueWithNext = new ValueWithNext(value);
		valueWithNext.setEndObj(object);
		element.element = valueWithNext;
		if (this.decisionPolicies.containsKey(key)) {
			AVLTree<ValueWithNext> tree = this.decisionPolicies.get(key);
			if (!tree.containsKey(element)) {
				tree.add(valueWithNext);
			}
		} else {
			AVLTree<ValueWithNext> tree = new AVLTree<ValueWithNext>();
			tree.add(valueWithNext);
			this.decisionPolicies.put(key, tree);
		}
		return null;
	}
	
	public PropertyComposite getPropertyComposite(String key, String value){
		PropertyComposite obj = null;
		if(this.decisionPolicies.containsKey(key)){
			AVLTree<ValueWithNext> tree = this.decisionPolicies.get(key);
			Entry<ValueWithNext> element = new Entry<ValueWithNext>();
			ValueWithNext valueWithNext = new ValueWithNext(value);
			element.element = valueWithNext;
			if(tree.containsKey(element)){
				obj = tree.get(element).getEndObj();
			}
		}
		return obj;
		
	}
	
	public class ValueWithNext implements Comparable<ValueWithNext> {
		private String value = null;
		private PropertyComposite endObj = null;
		private PropertyPathNode next = null;
		
		public ValueWithNext(String value){
			this.setValue(value);
		}
		
		public PropertyComposite getEndObj() {
			return endObj;
		}
		public void setEndObj(PropertyComposite endObj) {
			this.endObj = endObj;
		}
		public PropertyPathNode getNext() {
			return next;
		}
		public void setNext(PropertyPathNode next) {
			this.next = next;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public int compareTo(ValueWithNext o) {
			// TODO Auto-generated method stub
			return this.value.compareTo(o.getValue());
		}
	}
}
