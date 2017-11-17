package com.yeaxu.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> { 
	
	@Autowired
	private AuthenticationSuccessHandler yeaxuAuthenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		SmsCodeAuhenticationFilter smsCodeAuhenticationFilter  = new SmsCodeAuhenticationFilter();
		smsCodeAuhenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		smsCodeAuhenticationFilter.setAuthenticationSuccessHandler(yeaxuAuthenticationSuccessHandler);
		smsCodeAuhenticationFilter.setAuthenticationFailureHandler(yeaxuAuthenticationFailureHandler);
		SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
		smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);
		
		http
			.authenticationProvider(smsCodeAuthenticationProvider)
			.addFilterAfter(smsCodeAuhenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
}
