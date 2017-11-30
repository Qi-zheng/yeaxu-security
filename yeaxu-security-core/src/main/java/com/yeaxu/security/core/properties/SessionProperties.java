package com.yeaxu.security.core.properties;

public class SessionProperties {
	private int maximumSessions = 1;
	
	private boolean maxSessionsPreventsLogin = true;
	
	private String invalidSessionUrl = "/session/yeaxu-session-invalid";
	
	public String getInvalidSessionUrl() {
		return invalidSessionUrl;
	}

	public void setInvalidSessionUrl(String invalidSessionUrl) {
		this.invalidSessionUrl = invalidSessionUrl;
	}

	public int getMaximumSessions() {
		return maximumSessions;
	}

	public void setMaximumSessions(int maximumSessions) {
		this.maximumSessions = maximumSessions;
	}

	public boolean isMaxSessionsPreventsLogin() {
		return maxSessionsPreventsLogin;
	}

	public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
		this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
	}
	
}
