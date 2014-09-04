/*
*定义appframe控件事件（DBGRID、DBLISTBOX）与属性的关系对象
*/
function Ai_Tag_Event(){
	this.onrowclick = "S_OnRowClick";
	this.ondbclick = "S_OnGridDbClick";
	this.ontitledbclick = "S_OnTitleDbClick";
	this.onvalchange = "S_OnValueChange";
	this.onafterturnpage = "S_OnAfterTurnPage";
	this.onbeforeturnpage = "S_OnBeforeTurnPage";
	this.oncontextmenu = "S_OnContextMenu";
	this.ondblink = "S_OnDBLink";
	this.onfocusout = "S_OnFocusOut";
	this.onresize = "S_OnResize";
	this.onrowchange = "S_OnRowFocusChange";
	this.oncellchange = "S_OnCellFocusChange";
	this.onrowselected = "S_OnRowSelected";
	this.onchange = "OnItemChange";
	this.onclick = "OnItemClick";
	this.ondblclick = "OnItemDblClick";
}

/**
*页面对象
*pageId 当前页面对应的pageId
*/
function App_Page_Info(pageId){
	this.pageId = pageId;
	//appframe标签事件属性对象
	this.aiTagEvent = new Ai_Tag_Event();
	//定义一次事件方法中的共享变量对象
	this.sessionMap = new Object();
	//该页面中的绑定事件数组
	this.pageEvents = new Array();
	//定义驱动对象数组
	this.eventObjects = new Array();
	//定义当前正在执行的eventId
	this.exeEventId = -1;
	
	//添加页面绑定事件
	this.addPageEvent = function(eventId, pageEvent){
		this.pageEvents[eventId] = pageEvent;
	}
	
	//获取共享变量
	this.getParam = function(key){
		var paramValue;
		eval("paramValue=this.sessionMap." + key);
		return paramValue;
	}
	
	//保存共享变量
	this.putParam = function(key, value){
		eval("this.sessionMap." + key + "=value");
	}
	
	//一次事件方法结束后清除共享变量对象
	this.clearSessionMap = function(){
		this.sessionMap = new Object();
	}
	
	//添加共享变量对象
	this.addParams = function(obj){
		if(typeof obj != "object"){
			obj = eval("(" + obj + ")");
		}
		for(var o in obj){
			eval("this.sessionMap." + o + "=" + obj[o]);
		}
	}
	
	//重置共享变量对象
	this.putSessionMap = function(obj){
		if(typeof obj == "object"){
			this.sessionMap = obj;
		}else{
			this.sessionMap = eval("(" + obj + ")");
		}
	}
	
	//添加一个驱动对象
	this.addEventObject = function(eventObject){
		this.eventObjects.push(eventObject);
	}
	
	//为驱动对象添加属性 flag=true：新增一个驱动后在添加属性
	this.put = function(key, value, flag){
		if(flag){
			this.eventObjects[this.eventObjects.length] = new Object();
		}
		eval("this.eventObjects[this.eventObjects.length - 1]." + key + "=value");
	}
	
	//重置驱动对象
	this.clearEventObject = function(){
		this.eventObjects = new Array();
	}
	
	//获取appframe标签事件属性值
	this.getAiTagEventValue = function(eventName){
		var eventValue;
		eval("eventValue=this.aiTagEvent." + eventName);
		return eventValue;
	}
	
	//执行事件规则
	this.exeEventRule = function(eventId, paramObj){
		var eventObj = this.pageEvents[eventId];
		if(!eventObj){
			return true;
		}
		this.exeEventId = eventId;
		eventObj = eventObj.exeAllRule(this.pageId, paramObj, this.eventObjects);
		if(!eventObj.exeFlag){
			if(eventObj.errMsg){
				alert(eventObj.errMsg);
			}
			return false;
		}
		return true;
	}
}

//将Object对象转化为JSON串
function parseObjectToJson(srcObjs){
	var jsonStr = "[";
	for(var i = 0; i < srcObjs.length; i ++){
		jsonStr += "{";
		var srcObj = srcObjs[i];
		for(var attr in srcObj){
			jsonStr += '"' + attr + '":"' + srcObj[attr] + '",'
		}
		jsonStr = jsonStr.substr(0, jsonStr.length - 1);
		jsonStr += "}"
		if(i < srcObjs.length - 1){
			jsonStr += ","
		}
	}
	jsonStr += "]";
	return jsonStr;
}

function App_Page_Event(eventId, isExeRule, isLoadJsrule){
	this.eventId = eventId;//绑定事件编号
	this.isExeRule = isExeRule;//是否需要执行规则引擎方法
	this.jsRuleDataObj = null;//前台js规则数据对象
	this.hasJsRule = true;//是否有配置js规则
	this.exeFlag = true;//规则执行结果
	this.errMsg = "";//规则执行错误信息
	this.isLoadJsrule = isLoadJsrule;//是否只加载一次js规则到前台

	//执行所有规则
	this.exeAllRule = function(pageId, paramObj, eventObjects){
		this.exeFlag = true;
		this.errMsg = "";
		
		//不需要执行规则引擎方法，直接返回
		if(!this.isExeRule || this.isExeRule != 1){
			return this;
		}
		if(this.hasJsRule){//有配置js方法
			var curJsRuleDataObj = null;
			if(!this.isLoadJsrule || this.isLoadJsrule != 1){//需要每次触发事件后都重新加载js规则
				curJsRuleDataObj = queryZrulerData(pageId, eventId, isLoadJsrule, parseObjectToJson(eventObjects));
			}else{//js规则数据报错在前台
				if(!this.jsRuleDataObj){//直接获取前台保存的js规则数据为null，则从后台加载
					curJsRuleDataObj = this.jsRuleDataObj = queryZrulerData(pageId, eventId, isLoadJsrule, null);
				}else{//直接获取前台保存的js规则数据
					curJsRuleDataObj = this.jsRuleDataObj;
				}
				if(!curJsRuleDataObj){//前台、后台获取的js规则数据都为空，则表示没有配置js规则
					this.hasJsRule = false;
				}
			}
			if(curJsRuleDataObj){//如果有查询到js规则数据，则循环动态执行js规则
				for(var j = 0; j < curJsRuleDataObj.length; j ++){
					var ruleData = curJsRuleDataObj[j];
					var expressionStr = ruleData.expression.replace(/\$/g, "paramObj.");
					var ruleFlag = eval(expressionStr);
					if(!ruleFlag){
						if(ruleData.ruleAttentionType == "禁止型"){
							this.exeFlag = false;
							this.errMsg = ruleData.msgCode;
							return this;
						}else if(ruleData.ruleAttentionType == "确认型"){
							if(!confirm(ruleData.msgCode)){
								this.exeFlag = false;
								this.errMsg = null;
								return this;
							}
						}else{
							if(ruleData.msgCode){
								alert(ruleData.msgCode);
							}
						}
					}
				}
			}
		}
		this.exeAllJavaRule(pageId, paramObj, parseObjectToJson(eventObjects));
		return this;
	}
	
	//执行所有java规则
	this.exeAllJavaRule = function(pageId, paramObj, eventObjectJson){
		var strUrl = _gModuleName + "/zruler/com.asiainfo.zruler.web.ZrulerAction?action=checkJavaRule";
		strUrl += "&ZRULER_PAGE_ID=" +　pageId;
		strUrl += "&ZRULER_EVENT_ID=" + eventId;
		strUrl += "&ZRULER_EVENT_OBJECTS=" + eventObjectJson;
		strUrl += "&" + parseObjToString(paramObj);
		var retInfo = PostInfo(strUrl);
		var retFlag = retInfo.getValueByName("FLAG");
		if(retFlag != 'Y'){
			this.exeFlag = false;
			this.errMsg = retInfo.getValueByName("MSG");
		}
	}
}

function queryZrulerData(pageId, eventId, isLoadJsrule, eventObjectJson){
	var strUrl = _gModuleName + "/zruler/com.asiainfo.zruler.web.ZrulerAction?action=queryZrulerData&";
	strUrl += "ZRULER_PAGE_ID=" + pageId + "&ZRULER_EVENT_ID=" + eventId;
	strUrl += "&ZRULER_IS_LOAD_JSRULE=" + isLoadJsrule;
	strUrl += "&ZRULER_EVENT_OBJECTS=" + eventObjectJson;
	var retInfo = PostInfo(strUrl);
	var retFlag = retInfo.getValueByName("FLAG");
	if(retFlag != 'Y'){
		return null;
	}
	return eval("(" + retInfo.getValueByName("PAGE_ZRULER_DATA") + ")");
}

var g_PageInfoManager = null;

/*
*定义DBFORM属性页面事件方法的关联对象
*dbFormPK DBFORM控制的id
*dbFieldName DBFORM控件的水泥工
*paramFunc 获取参数方法
*busiFunc 业务方法
*busiParams  业务参数
*/
function AI_Form_Event(dbFormPK, dbFieldName, paramFunc, busiFunc, busiParams, eventId){
	this.dbFormPk = dbFormPK;
	this.dbFieldName = dbFieldName;
	this.paramFunc = paramFunc;
	this.busiFunc = busiFunc;
	this.busiParams = busiParams;
	this.eventId = eventId;
	
	this.dbFormActiveEvent = function(){
		bindEventFunction(paramFunc, busiFunc, busiParams, eventId);
	}
}

/*
*通过页面url加载界面元素事件
*hrefUrl 当前页面url
*parentUrl 父页面url
*pageType  页面类型 0：单独页面 (window) 1：内嵌页面iframe (window.parent) 2：open弹出页面 (window.opener) 3：showModalDialog弹出页面 (window.dialogArguments)
*/
function loadDocEvent(hrefUrl){
	if(!hrefUrl || typeof hrefUrl != 'string'){
		//获取当前页面url
		hrefUrl = window.location.pathname;
	}
	//获取页面唯一KEY值
	var pageInfoKey = getPageInfoKey("KEY_" + subUrlString(hrefUrl), window);
	//获取页面元素事件配置JSON数据
	var eventJson = queryPageEventData(pageInfoKey);
	if(eventJson){
		//将JSON数据转化为OBJECT对象
		var eventObj = eval("(" + eventJson + ")");
		//定义load方法，在页面事件绑定完成后执行（onload事件触发后继续绑定的load事件将不在执行）
		var loadEventFunction;
		//循环配置数据
		for(var i = 0; i < eventObj.length; i ++){
			//创建页面对象
			if(!g_PageInfoManager){
				g_PageInfoManager = new App_Page_Info(eventObj[i].PAGE_ID);
			}
			if(eventObj[i].ELEMENT_TYPE == 1){//元素类型为window或documentId
				var eventFunction;
				eval("eventFunction=function(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				if(eventObj[i].ELEMENT_ID != "window"){
					eventBind(eventObj[i].ELEMENT_ID, eventObj[i].ELEMENT_EVENT, eventFunction);
				}else{
					loadEventFunction = eventFunction;
				}
			}else if(eventObj[i].ELEMENT_TYPE == 2){//元素类型为DBTABLE
				var tableRowSetDiv = document.all("TableRowSet_" + eventObj[i].ELEMENT_ID);
				eval("window." + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "=function(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				eval("tableRowSetDiv." + g_PageInfoManager.getAiTagEventValue(eventObj[i].ELEMENT_EVENT) + "='" + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "'");
			}else if(eventObj[i].ELEMENT_TYPE == 3){//元素类型为DBFORM
				var elementId = eventObj[i].ELEMENT_ID;
				var feildId = "DBFORM_ALL";
				if(elementId.indexOf(".") > 0){
					var elementObjs = elementId.split(".");
					elementId = elementObjs[0];
					feildId = elementObjs[1];
				}
				var formObj = g_FormRowSetManager.get(elementId);
				var aiFormEvent = new AI_Form_Event(elementId, feildId, eventObj[i].PARAM_FUNC, eventObj[i].BUSI_FUNC, eventObj[i].BUSI_PARAMS, eventObj[i].EVENT_ID);
				if(feildId == "DBFORM_ALL"){//DBFORM所有FIELD的值改变事件
					formObj.dbFormAll = aiFormEvent;
				}else{//DBFORM单个FIELD的值改变事件
					var feildEventArr;
					if(formObj.feildEventArr){
						feildEventArr = formObj.feildEventArr;
					}else{
						feildEventArr = new Array();
					}
					feildEventArr.push(aiFormEvent);
					formObj.feildEventArr = feildEventArr;
				}
				eval("formObj.OnValueChangeFunc=dbFormOnValueChangeFunc");
			}else if(eventObj[i].ELEMENT_TYPE == 4){//元素类型为DBLISTBOX
				var listBoxObj = g_getListBox(eventObj[i].ELEMENT_ID);
				eval("window." + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "=function(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				eval("listBoxObj." + g_PageInfoManager.getAiTagEventValue(eventObj[i].ELEMENT_EVENT) + "='" + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "'");
			}else if(eventObj[i].ELEMENT_TYPE == 5){//提交校验规则
				eval("window.onCheckBeforeSubmit=function(){return bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				//var scrFunc = window.document.createElement("script");
				//try{
				//scrFunc.innerText = "function onCheckBeforeSubmit(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}";
				//}catch(e){alert("aaa");}
				//scrFunc.type = "text/javascript";
				//document.getElementsByTagName("head")[0].appendChild(scrFunc);
			}
			g_PageInfoManager.addPageEvent(eventObj[i].EVENT_ID, new App_Page_Event(eventObj[i].EVENT_ID, eventObj[i].IS_EXE_RULE, eventObj[i].IS_LOAD_JSRULE));
		}
		if(loadEventFunction && typeof loadEventFunction == 'function'){
			if(!loadEventFunction()){
				//document.getElementsByTagName("body")[0].style.display = "none";
			}
		}
	}
}

/*
*DBFORM值改变事件方法
*pFieldName 改变的属性ID
*pOldVal 改变前的值
*pNewVal 改变后的值
*dbFormPK DBFORM对象ID
*/
function dbFormOnValueChangeFunc(pFieldName, pOldVal, pNewVal, dbFormPK){
	var formObj = g_FormRowSetManager.get(dbFormPK);
	if(formObj.dbFormAll){//整个DBFORM对象的值改变事件
		var dbFormAll = formObj.dbFormAll;
		dbFormAll.dbFormActiveEvent();
	}
	if(formObj.feildEventArr){//单个DBFORM属性的值改变事件
		var feildEventArr = formObj.feildEventArr;
		for(var i = 0; i < feildEventArr.length; i ++){
			var dbFormField = feildEventArr[i];
			if(dbFormField.dbFormPk == dbFormPK && dbFormField.dbFieldName == pFieldName){
				dbFormField.dbFormActiveEvent();
			}
		}
	}
}

/*
*获取页面缓存唯一KEY值
*currKey 当前页面Url
*windowObj当前页面window对象
*/
function getPageInfoKey(currKey, windowObj){
	if(windowObj.opener){//弹出页面
			currKey += "_" + 2;
			currKey += "_" + subUrlString(windowObj.opener.location.pathname);
			currKey = getPageInfoKey(currKey, windowObj.opener)
	}else if(windowObj.dialogArguments){//弹出窗口
			currKey += "_" + 3;
			currKey += "_" + subUrlString(windowObj.dialogArguments.location.pathname);
			currKey = getPageInfoKey(currKey, windowObj.dialogArguments)
	}else if(windowObj.parent && windowObj.parent != windowObj && windowObj.parent != windowObj.top.parent.mainFrame){//内嵌页面
			currKey += "_" + 1;
			currKey += "_" + subUrlString(windowObj.parent.location.pathname);
			currKey = getPageInfoKey(currKey, windowObj.parent)
	}else{//单独页面
			currKey += "_" + 0;
			currKey += "_" + subUrlString(windowObj.location.pathname);	
	}
	return currKey;
}

/**
*根据页面URL获取jsp页面名称
*currUrl 页面URL
*/
function subUrlString(currUrl){
	if(currUrl.indexOf("/") >= 0){
		currUrl = currUrl.substr(currUrl.lastIndexOf("/") + 1);
	}
	return currUrl;
}

/*
*通过页面url查询界面元素事件数据
*hrefUrl 当前页面url
*parentUrl 父页面url
*pageType  页面类型 0：单独页面 (window) 1：内嵌页面iframe (window.parent) 2：open弹出页面 (window.opener) 3：showModalDialog弹出页面 (window.dialogArguments)
*return  JSON字符串
*/
function queryPageEventData(pageInfoKey){
	//http://localhost:8020
	var strUrl = _gModuleName + "/zruler/com.asiainfo.zruler.web.ZrulerAction?action=queryPageEventData";
	strUrl += "&pageInfoKey=" + pageInfoKey;
	var retInfo = PostInfo(strUrl);
	var retFlag = retInfo.getValueByName("FLAG");
	if(retFlag != 'Y'){
		alert(retInfo.getValueByName("MSG"));
		return null;
	}
	return retInfo.getValueByName("PAGE_EVENT_DATA");
}

/*
*获取业务evel方法参数列表字符串
*paramObj 保存参数的对象名
*funcParams 业务方法的参数列表
*/
function getFuncParams(paramObjStr, funcParams, paramObj){
	var paramStr = "(";
	if(funcParams && funcParams != 'undefined' && funcParams != 'null'){
		var paramArr = funcParams.split(",");
		for(var i = 0; i < paramArr.length; i ++){
			if(paramArr[i].indexOf(".") >= 0){
				if(paramArr[i].substring(0, paramArr[i].indexOf(".")) == "Const"){
					paramStr += "'" + paramArr[i].substr(paramArr[i].indexOf(".") + 1) + "'";
				}else if(paramArr[i].substring(0, paramArr[i].indexOf(".")) == "Param"){
					paramStr += paramObjStr + "." + paramArr[i].substr(paramArr[i].indexOf(".") + 1);
				}else{
					paramStr += paramArr[i];
				}
			}else{
				paramStr += paramArr[i];
			}
			if(i != (paramArr.length - 1)){
				paramStr += ", ";
			}
		}
	}else{
		if(paramObj){
			paramStr += paramObjStr;
		}
	}
	return paramStr + ")";
}

function getXmlHTTPRequest(){
	var xRequest = null;
	if(typeof ActiveXObject != "undifined"){
		xRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}else if(window.XMLHttpRequest){
		xRequest = new XMLHttpRequest();
	}
	return xRequest;
}

function PostZruler(url){
	var xRequest = getXmlHTTPRequest();
	var exeEventId = g_PageInfoManager.exeEventId;
	if(exeEventId && exeEventId != -1){
		var pageEventObj = g_PageInfoManager.pageEvents[exeEventId];
		if(pageEventObj && pageEventObj.isExeRule && pageEventObj.isExeRule == 1){
			if(url.indexOf("?") >= 0){
				url += "&";
			}else{
				url += "?";
			}
			url += "ZRULER_PAGE_ID=" + pageId + "&ZRULER_EVENT_ID=" + eventId;
			url += "&ZRULER_EVENT_OBJECTS=" + parseObjectToJson(g_PageInfoManager.eventObjects);
		}
	}
	xRequest.open("POST", url, false);
	xRequest.setRequestHeader("Content-Type", "multipart/form-data");
	xRequest.send(null);
	var retText = xRequest.responseText;
	var xmlInfo = getXMLDocument();
	return createUserDataClass(xmlInfo,true);
}

//获取xmlDom的标准方法
function getXMLDocument(){
	var xDoc=null;
	if(typeof ActiveXObject != "undefined"){
    	var msXmlAx=null;
    	//Newer Internet Explorer
   		try{
      		msXmlAx=new ActiveXObject("Msxml2.DOMDocument");   
    	}catch (e){
      		//Older Internet Explorer
     	 	msXmlAx=new ActiveXObject("Msxml.DOMDocument");   
   		}
   		xDoc=msXmlAx;
  	}else if(document.implementation && document.implementation.createDocument){ 
   		//Mozilla/Safari
   		xDoc=document.implementation.createDocument("","",null);   
 	}
  	if(xDoc==null || typeof xDoc.load=="undefined"){
    	xDoc=null;
 	}
  	return xDoc;
}

/*
*paramFuncName  获取参数的方法名或参数值
*busiFuncName   业务处理的方法名
*busiParams   业务处理方法的参数列表
*界面元素绑定的事件方法体
*/
function bindEventFunction(paramFuncName, busiFuncName, busiParams, eventId){
	var retFlag = true;
	var paramObj = null;
	//执行获取参数的方法
	if(paramFuncName && paramFuncName != 'undefined' && paramFuncName != 'null'){
		if(eval("typeof " + paramFuncName) == "function"){//paramFuncName为function类型
			eval("paramObj=" + paramFuncName + "()");
		}else{
			paramObj = paramFuncName;//直接获取参数
		}
	}
	paramObj = parseObjToObject(paramObj);
	//执行规则引擎方法
	if(g_PageInfoManager.exeEventRule(eventId, paramObj)){
		//执行业务方法
		if(busiFuncName && busiFuncName != 'undefined' && busiFuncName != 'null'){
			if(paramObj && typeof paramObj != 'object'){
				paramObj = parseObjToObject(paramObj);
			}
			eval(busiFuncName + getFuncParams("paramObj", busiParams, paramObj));
		}
	}else{
		retFlag = false;
	}
	g_PageInfoManager.clearEventObject();
	g_PageInfoManager.clearSessionMap();
	g_PageInfoManager.exeEventId = -1;
	return retFlag;
}

/*
*为界面元素绑定事件
*srcObj 绑定对象
*type   事件类型
*fn     事件方法
*/
function eventBind(srcObj, type, fn){
	if(srcObj){
		if(typeof srcObj == 'string'){
			if(srcObj == "window"){
				srcObj = window;
			}else{
				srcObj = document.getElementById(srcObj);
			}
		}
		if(srcObj){
			if(srcObj.addEventListener){
				srcObj.addEventListener(type, fn, false);
			}else if(srcObj.attachEvent){
				srcObj.attachEvent("on" + type, fn);
			}
		}
	}
}

/*
*将JSON字符串或key1=value1&key2=value2字符串转化为Object对象
*/
function parseObjToObject(srcObj){
	if(typeof srcObj == "object"){
		if(srcObj instanceof Object){
			return srcObj;
		}
	}else if(typeof srcObj == "string"){
		try{
			return eval("(" + srcObj + ")");
		}catch(e){
			srcObj = srcObj.replace("?", "");
			var destObj = new Object();
			var srcArr = srcObj.split("&");
			for(var i = 0; i < srcArr.length; i ++){
				var paramArr = srcArr[i].split("=");
				if(paramArr.length != 2){
					return null;
				}else{
					eval("destObj." + paramArr[0] + " = '" + paramArr[1] + "'");
				}
			}
			return destObj;
		}
	}
	return null;
}

/*
*将Object对象或JSON对象转化为key1=value1&key2=value2字符串
*/
function parseObjToString(srcObj){
	if(typeof srcObj == "object"){
		if(srcObj instanceof Object){
			var destStr = "";
			for(var i in srcObj){
				destStr += i + "=" + srcObj[i] + "&";
			}
			return destStr.length > 0 ? destStr.substr(0, destStr.length - 1) : destStr;
		}else{
			return "";
		}
	}else if(typeof srcObj == "string"){
		try{
			var destObj = eval("(" + srcObj + ")");
			return parseObjToString(destObj);
		}catch(e){
			return srcObj;
		}
	}
	return "";
}

eventBind(window, "load", loadDocEvent);