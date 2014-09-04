package com.zb.dalisi.service;

/*
 * 服务启动时候调用，读取配置文件中的SOAEngine，开启服务
 */
public interface SOA {
	public void setSOAEngine(SOAEngine engine);
}
