package com.mhms.util;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String user_Nm;
	private String sessionId;
	private String deviceType;
	
	public String getUser_Nm() {
		return user_Nm;
	}
	public void setUser_Nm(String user_Nm) {
		this.user_Nm = user_Nm;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}
