package com.yeaxu.security.core.properties;

public class BrowserProperties {
	
	private String signUpUrl = "/yeaxu-signUp.html";
	
	private String signOutUrl;
	
	private String loginPage = "/yeaxu-signIn.html";//如果用户不指定登录页时的默认值
	
	private LoginType loginType = LoginType.JSON;
	
	private int rememberMeSeconds = 3600;
	
	private SessionProperties session = new SessionProperties();
	

	public SessionProperties getSession() {
		return session;
	}

	public void setSession(SessionProperties session) {
		this.session = session;
	}

	public String getSignUpUrl() {
		return signUpUrl;
	}

	public void setSignUpUrl(String signUpUrl) {
		this.signUpUrl = signUpUrl;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public String getSignOutUrl() {
		return signOutUrl;
	}

	public void setSignOutUrl(String signOutUrl) {
		this.signOutUrl = signOutUrl;
	}
	
}
