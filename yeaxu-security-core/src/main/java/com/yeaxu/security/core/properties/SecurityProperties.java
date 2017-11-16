package com.yeaxu.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yeaxu.security")
public class SecurityProperties {
	
	//��new����ʵ������������ļ�û�����ø�ֵ��ᱨ��ָ��
	private BrowserProperties browser = new BrowserProperties();;

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

}
