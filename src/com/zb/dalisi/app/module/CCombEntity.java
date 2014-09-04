package com.zb.dalisi.app.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.zb.dalisi.data.structure.Property;
import com.zb.dalisi.data.structure.PropertyComposite;

/*
 * ������һ�����ԡ�ֵ��������϶��ɵ�ʵ��Ļ��࣬�磺�������¼�����������
 */
public abstract class CCombEntity implements PropertyComposite {
	private String id;//ʵ��ID
	private Map<String, CCombEntityAttr> attrs = new HashMap<String, CCombEntityAttr>();//���ԡ�ֵ����������
	
	protected CCombEntity(String id){
		this.id = id;
	}
	
	public void addCombEntityAttr(CCombEntityAttr attr){
		if (this.attrs.containsKey(attr.getAttrId())){
			throw new RuleException("�ظ�����"+attr.getAttrId()+"������ֵ.");
		}
		this.attrs.put(attr.getAttrId(), attr);
	}
	
	protected String getId() {
		return id;
	}
	protected void setId(String id) {
		this.id = id;
	}
	public Map<String, CCombEntityAttr> getAttrs() {
		return attrs;
	}
	protected void setAttrs(Map<String, CCombEntityAttr> attrs) {
		this.attrs = attrs;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null){
			return false;
		}else if (obj == this){
			return true;
		}else if (obj instanceof CCombEntity){
			boolean bFlag = true;
			Set<Map.Entry<String, CCombEntityAttr>> set = attrs.entrySet();
			for (Iterator<Map.Entry<String, CCombEntityAttr>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, CCombEntityAttr> entry = (Map.Entry<String, CCombEntityAttr>) it.next();
				if (((CCombEntity) obj).getAttrs().containsKey(entry.getKey())) {
					if (((CCombEntity) obj).getAttrs().get(entry.getKey()).getAttrValue().equals(entry.getValue().getAttrValue())) {
						continue;
					}
				}
				bFlag = false;
				break;
			}
			return bFlag;
		}
		return super.equals(obj);
	}

	@Override
	public ArrayList<Property> getPropertys() {
		// TODO Auto-generated method stub
		ArrayList<Property> props = new ArrayList<Property>();
		
		Collection<CCombEntityAttr> tt = this.attrs.values();
		Iterator<CCombEntityAttr> iter = tt.iterator();
		while(iter.hasNext()){
			CCombEntityAttr a = iter.next();
			Property prop = new Property();
			prop.setKey(a.getAttrId());
			prop.setValue(a.getAttrValue().toString());
			props.add(prop);
		}
		
		return props;
	}
}
