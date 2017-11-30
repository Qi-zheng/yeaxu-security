package com.yeaxu.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

import com.yeaxu.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.yeaxu.security.core.properties.SecurityConstants;
import com.yeaxu.security.core.properties.SecurityProperties;
import com.yeaxu.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * 
 * @author seven
 *
 */
@Configuration
@EnableResourceServer
public class YeaxuResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Autowired
	protected AuthenticationSuccessHandler yeaxuAuthenticationSuccessHandler;
	@Autowired
	protected AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
	@Autowired
	private SpringSocialConfigurer yeaxuSocialSecurityConfig;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin()//启用表单登录
			// /yeaxu-signIn.html  换成/authentication/require  一个url中处理 则可以让用户实现配置登录页的目地
			.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
			.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
			.successHandler(yeaxuAuthenticationSuccessHandler)//设置自定义成功处理器 
			.failureHandler(yeaxuAuthenticationFailureHandler);
		http.apply(validateCodeSecurityConfig)
			.and()
				.apply(smsCodeAuthenticationSecurityConfig)
			.and()
				.apply(yeaxuSocialSecurityConfig)
			.and()
				.authorizeRequests()
					.antMatchers(
						SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
						SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
						SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
						securityProperties.getBrowser().getLoginPage(),
						securityProperties.getBrowser().getSignOutUrl(),
						securityProperties.getBrowser().getSignUpUrl(), 
						securityProperties.getBrowser().getSession().getInvalidSessionUrl(),
						"/user/regist")
						.permitAll()
					.anyRequest().authenticated()
			.and()
				.csrf().disable();
	}
}
