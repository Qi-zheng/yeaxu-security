package com.yeaxu.security.core.properties;

public class BrowserProperties {
	private String loginPage = "/yeaxu-signIn.html";//如果用户不指定登录页时的默认值
	
	private LoginType loginType = LoginType.JSON;

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
	
}
