package com.wso2telco.dep.tpservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import java.io.Serializable;

/**
 *
 */
public class ConfigDTO extends Configuration implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8356032431902326005L;
	

	@JsonProperty
	private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty
	private TLSMailConfigDTO tslMailConfig = new TLSMailConfigDTO();
	
	/*@JsonProperty
	private String host;
	
	@JsonProperty
	private int port;*/
	
	@JsonProperty
	private long waitingTimeForToken=200; //times in milliseconds ,default time is 200 ms
	
	@JsonProperty
	private Boolean isMaster;
	
	@JsonProperty
	private int tokenReadretrAttempts = 3; //default 3 times 
	
	@JsonProperty
	private long tokenReadretrAfter=60000; //times in milliseconds,default is set to one minit
	
	@JsonProperty
	private int retryAttempt=10000;
	
	@JsonProperty
	private long refreshWakeUpLeadTime=5000;

	@JsonProperty
	private String retryResponseCodes;

	@JsonProperty
	private  String authType = "mail.smtp.auth";

	
	public String getAuthType() {
		return authType;
	}

	public TLSMailConfigDTO getTLSMailConfigDTO(){
		return this.tslMailConfig;
	}

	public long getRefreshWakeUpLeadTime() {
		return refreshWakeUpLeadTime;
	}

	public void setRefreshWakeUpLeadTime(long refreshWakeUpLeadTime) {
		this.refreshWakeUpLeadTime = refreshWakeUpLeadTime;
	}

	public String getRetryResponseCodes() {
		return retryResponseCodes;
	}

	public void setRetryResponseCodes(String retryResponseCodes) {
		this.retryResponseCodes = retryResponseCodes;
	}

	public int getTokenReadretrAttempts() {
		return tokenReadretrAttempts;
	}

	public void setTokenReadretrAttempts(int tokenReadretrAttempts) {
		if (tokenReadretrAttempts > 3) {
			this.tokenReadretrAttempts = tokenReadretrAttempts;
		}
	}

	public long getTokenReadretrAfter() {
		return tokenReadretrAfter;
	}

	//token read retry mini limit is 1 min
	public void setTokenReadretrAfter(long tokenReadretrAfter) {
		if (tokenReadretrAfter > 60000) {
			this.tokenReadretrAfter = tokenReadretrAfter;
		}
	}
	public int getRetryAttempt(){
		return retryAttempt;
	}
	public Boolean getIsMaster() {
		return isMaster;
	}

	public Boolean isMaster() {
		return isMaster;
	}

	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
	}


	public long getWaitingTimeForToken() {
		return waitingTimeForToken;
	}

	public void setWaitingTimeForToken(long waitingTimeForToken) {
		if (waitingTimeForToken>200 &&waitingTimeForToken<20000)
		this.waitingTimeForToken = waitingTimeForToken;
	}

	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@Override
	public String toString() {
		return "ConfigDTO [database=" + database + ", waitingTimeForToken=" + waitingTimeForToken + ", isMaster="
				+ isMaster + ", tokenReadretrAttempts=" + tokenReadretrAttempts + ", tokenReadretrAfter="
				+ tokenReadretrAfter + ", retryAttempt=" + retryAttempt + "]";
	}
}
