package com.zb.dalisi.xml.cfg;


public class WebConfig {
	private Transaction transaction = null;
	private Datasource datasource = null;
	private Caches caches = null;

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public Caches getCaches() {
		return caches;
	}

	public void setCaches(Caches caches) {
		this.caches = caches;
	}
	
}

