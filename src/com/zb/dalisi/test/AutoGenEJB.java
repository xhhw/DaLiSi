package com.zb.dalisi.test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AutoGenEJB extends URLClassLoader {
	private String pkgName;
	private Set<String> importClasses;
	private List methodList;
	private String[] headLine={"@Stateless", "@Remote ({BookIntf.class})", "@Interceptors({AccountInterceptor.class})"};
	
	public AutoGenEJB(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		// TODO Auto-generated constructor stub
	}

	public void loadClassJar(String filename){
		try {
			JarFile file = new JarFile(filename);
			Enumeration<JarEntry> entrys = file.entries();
			JarEntry jarEntry = null;
			while (entrys.hasMoreElements()) {
				jarEntry = entrys.nextElement();
				if (!(jarEntry.isDirectory())){
					String tmpStr = jarEntry.getName();
					System.out.println(tmpStr);
				}
			}
			
//			URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
//			String filePath = URLDecoder.decode(url.getPath(), "UTF-8");
//			if(filePath.endsWith("*.jar")){
//				System.out.println(filePath);
//				
//			}
			
//			JarInputStream jis = new JarInputStream(this.getClass().getResourceAsStream(url));
//			JarEntry jarEntry = null;
//			while ((jarEntry = jis.getNextJarEntry()) != null) {
//				if (!(jarEntry.isDirectory())){
//					String tmpStr = jarEntry.getName();
//					System.out.println(tmpStr);
//				}
//			}
//			jis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URL[] url = null;
		new AutoGenEJB(url, Thread.currentThread().getContextClassLoader()).loadClassJar("D:/work/dom4j.jar");
	}

}
