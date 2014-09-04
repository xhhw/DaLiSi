package com.zb.dalisi.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBPageApp;
import com.zb.dalisi.db.bean.DBPageJsFunction;

public class PageJsCfg {
	private String pageUrl;
	private ArrayList<PageApp> pageApps =  new ArrayList<PageApp>();
	private Set<String> jsFiles = new HashSet<String>();
	
	public PageJsCfg(String url){
		this.setPageUrl(url);
	}
	
	public PageJsCfg buildPageJsCfg(String url){
		this.setPageUrl(url);
		if (this.getPageUrl() != null && !("".equals(this.getPageUrl()))) {
			ArrayList list = DBEngine.instance(DBPageApp.class).selectAll();
			for (int i=0;i<list.size();i++){
				if (((DBPageApp)list.get(i)).getPageUrl().equals(this.getPageUrl())){
					initPageJsCfg((DBPageApp)list.get(i));
				}
			}
		}
		return this;
	}
	
	private void initPageJsCfg(DBPageApp dbPageApp) {
		PageApp pageApp = new PageApp();
		pageApp.setPageUrl(dbPageApp.getPageUrl());
		pageApp.setElementId(dbPageApp.getElementId());
		pageApp.setEvent(dbPageApp.getEvent());
		pageApp.setFuncId(dbPageApp.getFuncId());
		pageApp.setPageType(dbPageApp.getPageType());
		pageApp.setPageParent(dbPageApp.getPageParent());
		
		ArrayList list = DBEngine.instance(DBPageJsFunction.class).selectAll();
		for (int i=0;i<list.size();i++){
			DBPageJsFunction f = (DBPageJsFunction)list.get(i);
			if (f.getFuncId().equals(dbPageApp.getFuncId())) {
				String jsFile = f.getJsFileName();
				String funcName = f.getJsFunction();
				String jsSrcType = f.getExt1();
				if ("".equals(jsSrcType))
					jsFiles.add(jsFile);
				pageApp.setFuncName(funcName);
				pageApp.setFileName(jsFile);
				pageApp.setJsSrcType(jsSrcType);
			}
		}
		pageApps.add(pageApp);
		System.out.println(pageApp.toString());
	}
	
	public Document toXML(){
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("page_js_cfg");
		root.addComment("This is a test for page_js_cfg");
		Element urlElement = root.addElement("url");
		urlElement.addAttribute("name", this.getPageUrl());
		urlElement.setText(this.getPageUrl());
		
		Element appsElement = root.addElement("page_apps");
		for (int i=0;i<this.getPageApps().size();i++){
			PageApp app = this.getPageApps().get(i);
			Element appElement = appsElement.addElement("page_app");
			appElement.addAttribute("pageUrl", app.getPageUrl());
			appElement.addAttribute("elementId", app.getElementId());
			appElement.addAttribute("event", app.getEvent());
			appElement.addAttribute("funcId", app.getFuncId());
			appElement.addAttribute("pageType", app.getPageType());
			appElement.addAttribute("pageParent", app.getPageParent());
			appElement.addAttribute("funcName", app.getFuncName());
			appElement.addAttribute("fileName", app.getFileName());
			appElement.addAttribute("jsSrcType", app.getJsSrcType());
		}
		
		Element jsfilesElement = root.addElement("js_files");
		for (Iterator<String> iter=this.getJsFiles().iterator();iter.hasNext();){
			String fileName = iter.next();
			Element fileElement = jsfilesElement.addElement("file");
			fileElement.addAttribute("filename", fileName);
		}
		
		return doc;
	}

	public Set<String> getJsFiles() {
		return jsFiles;
	}
	public void setJsFiles(Set<String> jsFiles) {
		this.jsFiles = jsFiles;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public ArrayList<PageApp> getPageApps() {
		return pageApps;
	}

	public void setPageApps(ArrayList<PageApp> pageApps) {
		this.pageApps = pageApps;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PageJsCfg demo = new PageJsCfg("");
		demo.buildPageJsCfg("a.html");
		System.out.println(demo.toXML().asXML());
	}
	
}
