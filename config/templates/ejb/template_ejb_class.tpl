package {{template_package_name}};

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

{{template_head_line}}
public class {{class_name}} implements {{interface_name}} {
	
{{template_param_list}}
	
{{template_function_impl_list}}

}
