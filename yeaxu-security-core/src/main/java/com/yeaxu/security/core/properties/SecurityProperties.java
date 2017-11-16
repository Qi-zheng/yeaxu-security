package com.yeaxu.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yeaxu.security")
public class SecurityProperties {
	
	//不new出来实例，如果配置文件没有配置该值则会报空指针
	private BrowserProperties browser = new BrowserProperties();;

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

}
