package com.yeaxu.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.yeaxu.security.browser.logout.YeaxuLogoutSuccessHandler;
import com.yeaxu.security.browser.session.YeaxuExpiredSessionStrategy;
import com.yeaxu.security.browser.session.YeaxuInvalidSessionStrategy;
import com.yeaxu.security.core.properties.SecurityProperties;

@Configuration
public class BrowserSecurityBeanConfig {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	@ConditionalOnMissingBean(InvalidSessionStrategy.class)
	public InvalidSessionStrategy invalidSessionStrategy () {
		return new YeaxuInvalidSessionStrategy(securityProperties.getBrowser().getSession().getInvalidSessionUrl());
	}
	
	@Bean
	@ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
		return new YeaxuExpiredSessionStrategy(securityProperties.getBrowser().getSession().getInvalidSessionUrl());
	}
	
	@Bean
	@ConditionalOnMissingBean(LogoutSuccessHandler.class)
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new YeaxuLogoutSuccessHandler(securityProperties.getBrowser().getSignOutUrl());
	}
}
