/**
 * 
 */
package com.yeaxu.security.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.yeaxu.security.core.properties.SecurityConstants;

/**
 * @author yeaxu
 *
 */
public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	protected AuthenticationSuccessHandler yeaxuAuthenticationSuccessHandler;
	
	@Autowired
	protected AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	
	protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
		http.formLogin()//启用表单登录
			// /yeaxu-signIn.html  换成/authentication/require  一个url中处理 则可以让用户实现配置登录页的目地
			.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
			.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
			.successHandler(yeaxuAuthenticationSuccessHandler)//设置自定义成功处理器 
			.failureHandler(yeaxuAuthenticationFailureHandler);
	}
	
}
