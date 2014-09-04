/*
*����appframe�ؼ��¼���DBGRID��DBLISTBOX�������ԵĹ�ϵ����
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
*ҳ�����
*pageId ��ǰҳ���Ӧ��pageId
*/
function App_Page_Info(pageId){
	this.pageId = pageId;
	//appframe��ǩ�¼����Զ���
	this.aiTagEvent = new Ai_Tag_Event();
	//����һ���¼������еĹ����������
	this.sessionMap = new Object();
	//��ҳ���еİ��¼�����
	this.pageEvents = new Array();
	//����������������
	this.eventObjects = new Array();
	//���嵱ǰ����ִ�е�eventId
	this.exeEventId = -1;
	
	//���ҳ����¼�
	this.addPageEvent = function(eventId, pageEvent){
		this.pageEvents[eventId] = pageEvent;
	}
	
	//��ȡ�������
	this.getParam = function(key){
		var paramValue;
		eval("paramValue=this.sessionMap." + key);
		return paramValue;
	}
	
	//���湲�����
	this.putParam = function(key, value){
		eval("this.sessionMap." + key + "=value");
	}
	
	//һ���¼�������������������������
	this.clearSessionMap = function(){
		this.sessionMap = new Object();
	}
	
	//��ӹ����������
	this.addParams = function(obj){
		if(typeof obj != "object"){
			obj = eval("(" + obj + ")");
		}
		for(var o in obj){
			eval("this.sessionMap." + o + "=" + obj[o]);
		}
	}
	
	//���ù����������
	this.putSessionMap = function(obj){
		if(typeof obj == "object"){
			this.sessionMap = obj;
		}else{
			this.sessionMap = eval("(" + obj + ")");
		}
	}
	
	//���һ����������
	this.addEventObject = function(eventObject){
		this.eventObjects.push(eventObject);
	}
	
	//Ϊ��������������� flag=true������һ�����������������
	this.put = function(key, value, flag){
		if(flag){
			this.eventObjects[this.eventObjects.length] = new Object();
		}
		eval("this.eventObjects[this.eventObjects.length - 1]." + key + "=value");
	}
	
	//������������
	this.clearEventObject = function(){
		this.eventObjects = new Array();
	}
	
	//��ȡappframe��ǩ�¼�����ֵ
	this.getAiTagEventValue = function(eventName){
		var eventValue;
		eval("eventValue=this.aiTagEvent." + eventName);
		return eventValue;
	}
	
	//ִ���¼�����
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

//��Object����ת��ΪJSON��
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
	this.eventId = eventId;//���¼����
	this.isExeRule = isExeRule;//�Ƿ���Ҫִ�й������淽��
	this.jsRuleDataObj = null;//ǰ̨js�������ݶ���
	this.hasJsRule = true;//�Ƿ�������js����
	this.exeFlag = true;//����ִ�н��
	this.errMsg = "";//����ִ�д�����Ϣ
	this.isLoadJsrule = isLoadJsrule;//�Ƿ�ֻ����һ��js����ǰ̨

	//ִ�����й���
	this.exeAllRule = function(pageId, paramObj, eventObjects){
		this.exeFlag = true;
		this.errMsg = "";
		
		//����Ҫִ�й������淽����ֱ�ӷ���
		if(!this.isExeRule || this.isExeRule != 1){
			return this;
		}
		if(this.hasJsRule){//������js����
			var curJsRuleDataObj = null;
			if(!this.isLoadJsrule || this.isLoadJsrule != 1){//��Ҫÿ�δ����¼������¼���js����
				curJsRuleDataObj = queryZrulerData(pageId, eventId, isLoadJsrule, parseObjectToJson(eventObjects));
			}else{//js�������ݱ�����ǰ̨
				if(!this.jsRuleDataObj){//ֱ�ӻ�ȡǰ̨�����js��������Ϊnull����Ӻ�̨����
					curJsRuleDataObj = this.jsRuleDataObj = queryZrulerData(pageId, eventId, isLoadJsrule, null);
				}else{//ֱ�ӻ�ȡǰ̨�����js��������
					curJsRuleDataObj = this.jsRuleDataObj;
				}
				if(!curJsRuleDataObj){//ǰ̨����̨��ȡ��js�������ݶ�Ϊ�գ����ʾû������js����
					this.hasJsRule = false;
				}
			}
			if(curJsRuleDataObj){//����в�ѯ��js�������ݣ���ѭ����ִ̬��js����
				for(var j = 0; j < curJsRuleDataObj.length; j ++){
					var ruleData = curJsRuleDataObj[j];
					var expressionStr = ruleData.expression.replace(/\$/g, "paramObj.");
					var ruleFlag = eval(expressionStr);
					if(!ruleFlag){
						if(ruleData.ruleAttentionType == "��ֹ��"){
							this.exeFlag = false;
							this.errMsg = ruleData.msgCode;
							return this;
						}else if(ruleData.ruleAttentionType == "ȷ����"){
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
	
	//ִ������java����
	this.exeAllJavaRule = function(pageId, paramObj, eventObjectJson){
		var strUrl = _gModuleName + "/zruler/com.asiainfo.zruler.web.ZrulerAction?action=checkJavaRule";
		strUrl += "&ZRULER_PAGE_ID=" +��pageId;
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
*����DBFORM����ҳ���¼������Ĺ�������
*dbFormPK DBFORM���Ƶ�id
*dbFieldName DBFORM�ؼ���ˮ�๤
*paramFunc ��ȡ��������
*busiFunc ҵ�񷽷�
*busiParams  ҵ�����
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
*ͨ��ҳ��url���ؽ���Ԫ���¼�
*hrefUrl ��ǰҳ��url
*parentUrl ��ҳ��url
*pageType  ҳ������ 0������ҳ�� (window) 1����Ƕҳ��iframe (window.parent) 2��open����ҳ�� (window.opener) 3��showModalDialog����ҳ�� (window.dialogArguments)
*/
function loadDocEvent(hrefUrl){
	if(!hrefUrl || typeof hrefUrl != 'string'){
		//��ȡ��ǰҳ��url
		hrefUrl = window.location.pathname;
	}
	//��ȡҳ��ΨһKEYֵ
	var pageInfoKey = getPageInfoKey("KEY_" + subUrlString(hrefUrl), window);
	//��ȡҳ��Ԫ���¼�����JSON����
	var eventJson = queryPageEventData(pageInfoKey);
	if(eventJson){
		//��JSON����ת��ΪOBJECT����
		var eventObj = eval("(" + eventJson + ")");
		//����load��������ҳ���¼�����ɺ�ִ�У�onload�¼�����������󶨵�load�¼�������ִ�У�
		var loadEventFunction;
		//ѭ����������
		for(var i = 0; i < eventObj.length; i ++){
			//����ҳ�����
			if(!g_PageInfoManager){
				g_PageInfoManager = new App_Page_Info(eventObj[i].PAGE_ID);
			}
			if(eventObj[i].ELEMENT_TYPE == 1){//Ԫ������Ϊwindow��documentId
				var eventFunction;
				eval("eventFunction=function(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				if(eventObj[i].ELEMENT_ID != "window"){
					eventBind(eventObj[i].ELEMENT_ID, eventObj[i].ELEMENT_EVENT, eventFunction);
				}else{
					loadEventFunction = eventFunction;
				}
			}else if(eventObj[i].ELEMENT_TYPE == 2){//Ԫ������ΪDBTABLE
				var tableRowSetDiv = document.all("TableRowSet_" + eventObj[i].ELEMENT_ID);
				eval("window." + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "=function(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				eval("tableRowSetDiv." + g_PageInfoManager.getAiTagEventValue(eventObj[i].ELEMENT_EVENT) + "='" + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "'");
			}else if(eventObj[i].ELEMENT_TYPE == 3){//Ԫ������ΪDBFORM
				var elementId = eventObj[i].ELEMENT_ID;
				var feildId = "DBFORM_ALL";
				if(elementId.indexOf(".") > 0){
					var elementObjs = elementId.split(".");
					elementId = elementObjs[0];
					feildId = elementObjs[1];
				}
				var formObj = g_FormRowSetManager.get(elementId);
				var aiFormEvent = new AI_Form_Event(elementId, feildId, eventObj[i].PARAM_FUNC, eventObj[i].BUSI_FUNC, eventObj[i].BUSI_PARAMS, eventObj[i].EVENT_ID);
				if(feildId == "DBFORM_ALL"){//DBFORM����FIELD��ֵ�ı��¼�
					formObj.dbFormAll = aiFormEvent;
				}else{//DBFORM����FIELD��ֵ�ı��¼�
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
			}else if(eventObj[i].ELEMENT_TYPE == 4){//Ԫ������ΪDBLISTBOX
				var listBoxObj = g_getListBox(eventObj[i].ELEMENT_ID);
				eval("window." + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "=function(){bindEventFunction('" + eventObj[i].PARAM_FUNC + "','" + eventObj[i].BUSI_FUNC + "','" + eventObj[i].BUSI_PARAMS + "','" + eventObj[i].EVENT_ID + "');}");
				eval("listBoxObj." + g_PageInfoManager.getAiTagEventValue(eventObj[i].ELEMENT_EVENT) + "='" + eventObj[i].ELEMENT_ID + "_" + eventObj[i].ELEMENT_EVENT + "'");
			}else if(eventObj[i].ELEMENT_TYPE == 5){//�ύУ�����
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
*DBFORMֵ�ı��¼�����
*pFieldName �ı������ID
*pOldVal �ı�ǰ��ֵ
*pNewVal �ı���ֵ
*dbFormPK DBFORM����ID
*/
function dbFormOnValueChangeFunc(pFieldName, pOldVal, pNewVal, dbFormPK){
	var formObj = g_FormRowSetManager.get(dbFormPK);
	if(formObj.dbFormAll){//����DBFORM�����ֵ�ı��¼�
		var dbFormAll = formObj.dbFormAll;
		dbFormAll.dbFormActiveEvent();
	}
	if(formObj.feildEventArr){//����DBFORM���Ե�ֵ�ı��¼�
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
*��ȡҳ�滺��ΨһKEYֵ
*currKey ��ǰҳ��Url
*windowObj��ǰҳ��window����
*/
function getPageInfoKey(currKey, windowObj){
	if(windowObj.opener){//����ҳ��
			currKey += "_" + 2;
			currKey += "_" + subUrlString(windowObj.opener.location.pathname);
			currKey = getPageInfoKey(currKey, windowObj.opener)
	}else if(windowObj.dialogArguments){//��������
			currKey += "_" + 3;
			currKey += "_" + subUrlString(windowObj.dialogArguments.location.pathname);
			currKey = getPageInfoKey(currKey, windowObj.dialogArguments)
	}else if(windowObj.parent && windowObj.parent != windowObj && windowObj.parent != windowObj.top.parent.mainFrame){//��Ƕҳ��
			currKey += "_" + 1;
			currKey += "_" + subUrlString(windowObj.parent.location.pathname);
			currKey = getPageInfoKey(currKey, windowObj.parent)
	}else{//����ҳ��
			currKey += "_" + 0;
			currKey += "_" + subUrlString(windowObj.location.pathname);	
	}
	return currKey;
}

/**
*����ҳ��URL��ȡjspҳ������
*currUrl ҳ��URL
*/
function subUrlString(currUrl){
	if(currUrl.indexOf("/") >= 0){
		currUrl = currUrl.substr(currUrl.lastIndexOf("/") + 1);
	}
	return currUrl;
}

/*
*ͨ��ҳ��url��ѯ����Ԫ���¼�����
*hrefUrl ��ǰҳ��url
*parentUrl ��ҳ��url
*pageType  ҳ������ 0������ҳ�� (window) 1����Ƕҳ��iframe (window.parent) 2��open����ҳ�� (window.opener) 3��showModalDialog����ҳ�� (window.dialogArguments)
*return  JSON�ַ���
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
*��ȡҵ��evel���������б��ַ���
*paramObj ��������Ķ�����
*funcParams ҵ�񷽷��Ĳ����б�
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

//��ȡxmlDom�ı�׼����
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
*paramFuncName  ��ȡ�����ķ����������ֵ
*busiFuncName   ҵ����ķ�����
*busiParams   ҵ�������Ĳ����б�
*����Ԫ�ذ󶨵��¼�������
*/
function bindEventFunction(paramFuncName, busiFuncName, busiParams, eventId){
	var retFlag = true;
	var paramObj = null;
	//ִ�л�ȡ�����ķ���
	if(paramFuncName && paramFuncName != 'undefined' && paramFuncName != 'null'){
		if(eval("typeof " + paramFuncName) == "function"){//paramFuncNameΪfunction����
			eval("paramObj=" + paramFuncName + "()");
		}else{
			paramObj = paramFuncName;//ֱ�ӻ�ȡ����
		}
	}
	paramObj = parseObjToObject(paramObj);
	//ִ�й������淽��
	if(g_PageInfoManager.exeEventRule(eventId, paramObj)){
		//ִ��ҵ�񷽷�
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
*Ϊ����Ԫ�ذ��¼�
*srcObj �󶨶���
*type   �¼�����
*fn     �¼�����
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
*��JSON�ַ�����key1=value1&key2=value2�ַ���ת��ΪObject����
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
*��Object�����JSON����ת��Ϊkey1=value1&key2=value2�ַ���
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