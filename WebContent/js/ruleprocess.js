(function(R) {
	R.fn.connection = function (obj1, obj2, line, bg) {
	    if (obj1.line && obj1.from && obj1.to) {
	        line = obj1;
	        obj1 = line.from;
	        obj2 = line.to;
	    }
	    var bb1 = obj1.getBBox(),
	        bb2 = obj2.getBBox(),
	        p = [{x: bb1.x + bb1.width / 2, y: bb1.y - 1},
	        {x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1},
	        {x: bb1.x - 1, y: bb1.y + bb1.height / 2},
	        {x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2},
	        {x: bb2.x + bb2.width / 2, y: bb2.y - 1},
	        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
	        {x: bb2.x - 1, y: bb2.y + bb2.height / 2},
	        {x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2}],
	        d = {}, dis = [];
	    for (var i = 0; i < 4; i++) {
	        for (var j = 4; j < 8; j++) {
	            var dx = Math.abs(p[i].x - p[j].x),
	                dy = Math.abs(p[i].y - p[j].y);
	            if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x) && ((i != 2 && j != 7) || p[i].x > p[j].x) && ((i != 0 && j != 5) || p[i].y > p[j].y) && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
	                dis.push(dx + dy);
	                d[dis[dis.length - 1]] = [i, j];
	            }
	        }
	    }
	    if (dis.length == 0) {
	        var res = [0, 4];
	    } else {
	        res = d[Math.min.apply(Math, dis)];
	    }
	    var x1 = p[res[0]].x,
	        y1 = p[res[0]].y,
	        x4 = p[res[1]].x,
	        y4 = p[res[1]].y;
	    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
	    dy = Math.max(Math.abs(y1 - y4) / 2, 10);
	    var x2 = [x1, x1, x1 - dx, x1 + dx][res[0]].toFixed(3),
	        y2 = [y1 - dy, y1 + dy, y1, y1][res[0]].toFixed(3),
	        x3 = [0, 0, 0, 0, x4, x4, x4 - dx, x4 + dx][res[1]].toFixed(3),
	        y3 = [0, 0, 0, 0, y1 + dy, y1 - dy, y4, y4][res[1]].toFixed(3);
	    var path = ["M", x1.toFixed(3), y1.toFixed(3), "C", x2, y2, x3, y3, x4.toFixed(3), y4.toFixed(3), "M", x3, y3, "L", x4.toFixed(3), y4.toFixed(3)].join(",");
//	    var path2 = ["M", x3, y3, "L", x4.toFixed(3), y4.toFixed(3)].join(",");
	    if (line && line.line) {
	        line.bg && line.bg.attr({path: path});
	        line.line.attr({path: path});
//	        line.line2.attr({path: path2});
	    } else {
	        var color = typeof line == "string" ? line : "#000";
	        return {
	            bg: bg && bg.split && this.path(path).attr({stroke: bg.split("|")[0], fill: "none", "stroke-width": bg.split("|")[1] || 3}),
	            line: this.path(path).attr({stroke: color, fill: "none"}),
//	            line2: this.path(path2).attr({stroke: color, fill: "none"}),
	            from: obj1,
	            to: obj2
	        };
	    }
	};
	
    R.el.draggable = function(move, start, up) {
        this._ui = this._ui || {};
        var that = this;
        this._ui.onMove = R.is(move, 'function') ?
        move : function(distanceX, distanceY, x, y, deltaX, deltaY) {
            that.translate(deltaX, deltaY);
        };
        this._ui.onStart = R.is(start, 'function') ? start : function(x, y) {
        };
        function onMove(distanceX, distanceY, x, y) {
            var deltaX = x - that._ui.lastX;
            var deltaY = y - that._ui.lastY;
            that._ui.lastX = x;
            that._ui.lastY = y;
            that._ui.onMove(distanceX, distanceY, x, y, deltaX, deltaY);
            for (var i = connections.length; i--;) {
            	R.fn.connection(connections[i]);
            }
            
            var txt = this.data("node_name");
    		if (txt != undefined && txt.type == "text"){
    			debugger;
		    	var bb = that.getBBox();
		    	txt.remove();
	    		txt = that.paper.text(bb.x+5, bb.y+bb.height/2, that.data("rule_id")).attr({font: "12px Fontin-Sans, Arial", fill: "#fff", "text-anchor": "start"});
	    		that.data("node_name", txt);
    		}
    		
            that.paper.safari();
        };
        function onStart(x, y) {
            that._ui.lastX = x;
            that._ui.lastY = y;
            that._ui.onStart(x, y);
        };
        return this.drag(onMove, onStart, up);
    };
})(Raphael);

//var ruleMenuShapes[],ruleProcessShapes[];
var mypaper,
    mydata = Raphael.fn.set(), 
    connections = [], 
    lineEl = Raphael.fn.set();

function onSaveProcess(){
	debugger;
	mydata.attr({fill: "blue"});
	var str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+"<rule-process id=\"test_id\" version=\"1.0\">"
		+"</rule-process>";
	doc = new xml(str);
//	printXML(doc);
	mydata.forEach(addElToXmlDoc);
	printXML(doc);
//	alert(doc);
//	alert(doc.xml);
}

function addElToXmlDoc(el){
	el.attr({fill: "red"});
	var rootNode = doc.getElementsByTagName("rule-process")[0];
	
	var ruleNode = document.createElementNS('','node');
	ruleNode.setAttribute("id", el.id);
	if (el.data("rule_id") != undefined){
		ruleNode.setAttribute("rule_id", el.data("rule_id"));
		var newText = document.createTextNode("ID is "+el.data("rule_id"));
		ruleNode.appendChild(newText);
	}

	rootNode.appendChild(ruleNode);
}

function onRemoveProcess(){
	debugger;
	lineEl.clear();
	
	mydata.forEach(function(el){
		var txt = el.data("node_name");
		if (txt != undefined && txt.type == "text"){
	    	txt.remove();
		}
	});
	mydata.remove();
	mydata.clear();
	
	for (var i=0, len=connections.length; i<len; i++){
		var tmp = connections.pop();
		tmp.line.remove();
		tmp.bg.remove();
		delete tmp;
	}
}

function onDrawLineBegin(){
	debugger;
	mydata.click(function () {
		lineEl.push(mypaper.getElementByPoint(event.clientX, event.clientY).attr({stroke: "#fff"}));
		if (lineEl.length==2){
			onDrawLineEnd();
		}
    });
}
function onDrawLineEnd(){
	debugger;
	mydata.unclick();
	if (lineEl.length>=2){
		connections.push(mypaper.connection(lineEl[0], lineEl[1], "#000", "#fff"));
	}
	lineEl.clear();
}

function onDbClickEl(el){
	//alert(this.data("rule_id"));
	debugger;
//	do {
//		var name = prompt("please enter the rule_id.");
//		var correct = confirm("You entered '"+name+"'.\n"+"Click Okay to proceed or Cancel to re-enter.");
//	}while(!correct);
//	
	var name = prompt("please enter the rule_id.");
	if (name == this.data("rule_id")){
//		alert("Hello, "+name);
		var txt = this.data("node_name");
		if (txt == undefined || (typeof txt !== "object" && typeof txt !== "function")){
			
    		var bb = this.getBBox();
    		txt = this.paper.text(bb.x+5, bb.y+bb.height/2, this.data("rule_id")).attr({font: "12px Fontin-Sans, Arial", fill: "#fff", "text-anchor": "start"});
    		this.scale((txt.getBBox().width+20)/bb.width,(txt.getBBox().height+10)/bb.height);
    		txt.remove();
    		bb = this.getBBox();
    		txt = this.paper.text(bb.x+5, bb.y+bb.height/2, this.data("rule_id")).attr({font: "12px Fontin-Sans, Arial", fill: "#fff", "text-anchor": "start"});
    		this.data("node_name", txt);
		}
	} else {
		this.data("rule_id",name);
		var txt = this.data("node_name");
		if (txt != undefined && txt.type == "text"){
			txt.remove();
		}
		var bb = this.getBBox();
		txt = this.paper.text(bb.x+5, bb.y+bb.height/2, this.data("rule_id")).attr({font: "12px Fontin-Sans, Arial", fill: "#fff", "text-anchor": "start"});
		this.scale((txt.getBBox().width+20)/bb.width,(txt.getBBox().height+10)/bb.height);
		txt.remove();
		bb = this.getBBox();
		txt = this.paper.text(bb.x+5, bb.y+bb.height/2, this.data("rule_id")).attr({font: "12px Fontin-Sans, Arial", fill: "#fff", "text-anchor": "start"});
		this.data("node_name", txt);
	}
}

window.onload = function () {
	var r = Raphael("processholder", 640, 480);
	mypaper = r;
	debugger;
	r.circle(40, 30, 10).attr({fill: "#0f0", stroke: "#000", "fill-opacity": 0.5}).click(function () {
		mydata.push(this.clone().dblclick(onDbClickEl).draggable());
    });
	r.ellipse(40, 70, 20, 10).attr({fill: "#f00", stroke: "#000", "fill-opacity": 0.5}).click(function () {
		mydata.push(this.clone().dblclick(onDbClickEl).draggable());
    });
	var pathRhombus = "M40,100 l30,10 -30,10 -30,-10z";
    r.path(pathRhombus).attr({fill: "#0ff", stroke: "#000", "fill-opacity": 0.5}).click(function () {
    	mydata.push(this.clone().dblclick(onDbClickEl).draggable());
    });
    r.rect(15, 140, 50, 20, 2).attr({fill: "#f0f", stroke: "#000", "fill-opacity": 0.5, cursor: "move"}).click(function () {
    	mydata.push(this.clone().dblclick(onDbClickEl).draggable());
    });
    var pathArrow = "M15,190 l40,0 0,-10 10,13 -10,13 0,-10 -40,0z";
    r.path(pathArrow).attr({fill: "#fff", stroke: "#fff", "fill-opacity": 0.8}).click(function () {
    	onDrawLineBegin();
    });
    
//    var testxxx = "M,428.97,139,C,404.495,139,404.495,221,380.02,221,M,404.495,221,L,380.02,221";
//    r.path(testxxx).attr({fill: "#fff", stroke: "#fff", "fill-opacity": 1})
    
    var pathLineV = "M85,0 V480";
    r.path(pathLineV).attr({fill: "#fff", stroke: "#fff", "fill-opacity": 1})
};
