package com.zb.dalisi.db.bean;

import com.zb.dalisi.db.DBBase;

public class DB{{class_name}} extends DBBase {
	
{{template_param_list}}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("******{{table_name}}*********"
{{template_tostring_list}}
            );
		return buf.toString();
	}
	
{{template_getfunction_list}}

{{template_setfunction_list}}

}

