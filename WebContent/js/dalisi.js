/*!
 * Dalisi JavaScript Library v1.0.0
 * 
 * Copyright 2014, ZhangBin
 * 
 * Date: 2014-01-01 08:21:08
 */
(function (glob){
	function D(){
	};
	D.version = "1.0.0";
	D.jsFiles = [];
	D.jsApps = [];
	D.domainUrl = "";
	
	var proto = "prototype",
		g = {
            doc: document,
            win: window
        };
	
	D.init = function(){
		// Mozilla, Opera and webkit nightlies currently support this event
		if ( document.addEventListener ) {
			// Use the handy event callback
			document.addEventListener( "DOMContentLoaded", DOMContentLoaded, false );
			
			// A fallback to window.onload, that will always work
			window.addEventListener( "load", jQuery.ready, false );

		// If IE event model is used
		} else if ( document.attachEvent ) {
			// ensure firing before onload,
			// maybe late but safe also for iframes
			document.attachEvent("onreadystatechange", DOMContentLoaded);
			
			// A fallback to window.onload, that will always work
			window.attachEvent( "onload", jQuery.ready );

			// If IE and not a frame
			// continually check to see if the document is ready
			var toplevel = false;

			try {
				toplevel = window.frameElement == null;
			} catch(e) {}

			if ( document.documentElement.doScroll && toplevel ) {
				doScrollCheck();
			}
		}
	}
	
	D.ready = function(fn){
		
	}
	
	_item = function(){
		var pageUrl,elementId,event,funcId,pageType,pageParent;
	}
	//D.page = [];
	var _page = function(){
		debugger;
		var _protocol = (("https:" == g.win.location.protocol) ? "https://" : "http://");
		var _domain = "localhost:8020/jetty/";
		
		var url = g.win.location.href;
		if (url != undefined && url.indexOf("file:") >= 0){
			var _protocol = "file:///D:/";
			var _domain = "eclipse-jee-galileo-win32/myproject/workspace/jettyDemo/jetty/web/";
		}
		
		if (url != undefined && url.lastIndexOf("/") >= 0){
			var prefix = _protocol + _domain;
			D.domainUrl = prefix;
			var pageUrl = url.substring(prefix.length);
		}
		return pageUrl;
	}();
	
	g.win.onload = function(){
		debugger;
		var request = new XMLHttpRequest();
		request.open("POST","http://localhost:8020/dalisi");
		request.onreadystatechange = function(){
			if(request.readyState === 4 && request.status === 200){
				var type = request.getResponseHeader("Content-Type");
				if (type.indexOf("xml") !== -1 && request.responseXML){
					registPageEventListener(request.responseXML);
				}
				else if (type === "application/json"){
					alert(JSON.parse(request.responseText));
				}
				else {
					alert(request.responseText);
				}
			}
		};
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		request.send("pageUrl="+_page);
	}
	
	function registPageEventListener(xmldoc){
		debugger;
		//根据XML生成JS文件列表
		var files = [];
		var fileNodes = xmldoc.getElementsByTagName("file");
		for ( var len = fileNodes.length, i = 0; i < len; i++) {
			var name = fileNodes[i].getAttribute("filename");
			D.jsFiles.push(name);
		}
		
		//根据XML生成注册事件列表
		var apps = [];
		var appNodes = xmldoc.getElementsByTagName("page_app");
		for ( var len = appNodes.length, i = 0; i < len; i++) {
			var n = appNodes[i];
			var a = {
				pageUrl : n.getAttribute("pageUrl"),
				elementId : n.getAttribute("elementId"),
				event : n.getAttribute("event"),
				funcId : n.getAttribute("funcId"),
				pageType : n.getAttribute("pageType"),
				pageParent : n.getAttribute("pageParent"),
				funcName : n.getAttribute("funcName"),
				fileName : n.getAttribute("fileName"),
				jsSrcType: n.getAttribute("jsSrcType")
			};
			D.jsApps.push(a);
		}
		
		//加载JS文件
		includeJSfiles();
		
		//注册事件函数
		registPageEvent();
	}
	
	function sleep(n){
        var start = new Date().getTime();
        while(true){
        	if(new Date().getTime() - start > n) break;
        }
    }
	
	function includeJSfiles(){
		debugger;
		for (var i=0; i<D.jsFiles.length; i++){
			var el = g.doc.createElement('script');
			el.src = D.jsFiles[i];
			el.type = 'text/javascript';
			var head = g.doc.getElementsByTagName('head')[0];
			head.appendChild(el);
			if (navigator.userAgent.indexOf("IE")>=0){
				el.onreadystatechange = function(){
					if (el && (el.readyState=="loaded"||el.readyState=="complete")){
						el.onreadystatechange = null;
						var fileUrl = el.src.substring(D.domainUrl.length);
						registPageEvent(fileUrl);
					}
				};
			}else{
				el.onload = function(){
					el.onload = null;
					var fileUrl = el.src.substring(D.domainUrl.length);
					registPageEvent(fileUrl);
				};
			}
		}
	}
	
	function addJSFunction(app){
		var el = g.doc.createElement('script');
		el.innerText = app.funcName;
		el.type = 'text/javascript';
		var head = g.doc.getElementsByTagName('head')[0];
		head.appendChild(el);
		if (navigator.userAgent.indexOf("IE")>=0){
			el.onreadystatechange = function(){
				if (el && (el.readyState=="loaded"||el.readyState=="complete")){
					el.onreadystatechange = null;
					registPageEventApp(app);
				}
			};
		}else{
			el.onload = function(){
				el.onload = null;
				registPageEventApp(app);
			};
		}
	}
	
	function registPageEventApp(app){
		debugger;
		var tmpEl = g.doc.getElementById(app.elementId);
		if (tmpEl == undefined)
			return;
		if (tmpEl.attachEvent){
			tmpEl.attachEvent("onclick",eval(app.funcName),false);
		}else{
			tmpEl.addEventListener("click",eval(app.funcName),false);
		}
	}
	
	function registPageEvent(file){
		debugger;
		D.jsApps.forEach(function(app){
			debugger;
			if (file == undefined || app.fileName == null){
				if (app.fileName == undefined || app.fileName == null){
					addJSFunction();
				}
				return;
			}
			if (app.fileName != file)
				return;
			var tmpEl = g.doc.getElementById(app.elementId);
			if (tmpEl == undefined)
				return;
			if (tmpEl.attachEvent){
				tmpEl.attachEvent("onclick",eval(app.funcName),false);
			}else{
				tmpEl.addEventListener("click",eval(app.funcName),false);
			}
		});
	}

	g.win.Dalisi = D;
	
})(window);

