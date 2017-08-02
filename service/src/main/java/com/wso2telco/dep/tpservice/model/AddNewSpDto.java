package com.wso2telco.dep.tpservice.model;

import java.util.ArrayList;

public class AddNewSpDto {
	
	private String ownerId;
	private String tokenUrl;
	private long defaultconnectionresettime;
	private int retryAttmpt;
	private int retrymax;
	private int retrydelay;
	private  ArrayList<TokenDTO> spTokenList ;
	
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getTokenUrl() {
		return tokenUrl;
	}
	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}
	public long getDefaultconnectionresettime() {
		return defaultconnectionresettime;
	}
	public void setDefaultconnectionresettime(int defaultconnectionresettime) {
		this.defaultconnectionresettime = defaultconnectionresettime;
	}
	public int getRetryAttmpt() {
		return retryAttmpt;
	}
	public void setRetryAttmpt(int retryAttmpt) {
		this.retryAttmpt = retryAttmpt;
	}
	public int getRetrymax() {
		return retrymax;
	}
	public void setRetrymax(int retrymax) {
		this.retrymax = retrymax;
	}
	public int getRetrydelay() {
		return retrydelay;
	}
	public void setRetrydelay(int retrydelay) {
		this.retrydelay = retrydelay;
	}
	public ArrayList<TokenDTO> getSpTokenList() {
		return spTokenList;
	}
	public void setSpTokenList(ArrayList<TokenDTO> spTokenList) {
		this.spTokenList = spTokenList;
	}
	
	

}
