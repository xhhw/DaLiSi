package com.zb.dalisi.js;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RuleJsExpression {
	
	public RuleJsExpression(){
		
	}
	public static void run(String jsstring){
		ScriptEngineManager mgr = new ScriptEngineManager();  
		ScriptEngine engine = mgr.getEngineByExtension("js");//js
		
		/*
        * application/javascript
		* application/ecmascript
		* text/javascript
		* text/ecmascript
		*/
//		ScriptEngine engine2 = mgr.getEngineByMimeType("application/javascript");
		
		/*
		 * js
		 * rhino
		 * JavaScript
		 * javascript
		 * ECMAScript//js和javascript是它的扩展
        */
//		ScriptEngine engine3 = mgr.getEngineByName("JavaScript");
		
//		engine.eval(new FileReader(jsfile));
		
		try {
			engine.eval(jsstring);
			Invocable inv = (Invocable) engine;   
			String value = String.valueOf(inv.invokeFunction("test"));
			System.out.println(value);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	
}
