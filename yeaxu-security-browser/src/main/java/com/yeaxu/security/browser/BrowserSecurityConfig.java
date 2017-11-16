package com.yeaxu.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.yeaxu.security.core.properties.SecurityProperties;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationSuccessHandler yeaxuAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()//启用表单登录
			// /yeaxu-signIn.html  换成/authentication/require  一个url中处理 则可以让用户实现配置登录页的目地
			.loginPage("/authentication/require")  
			.loginProcessingUrl("/authentication/form") 
			.successHandler(yeaxuAuthenticationSuccessHandler)  //设置自定义成功处理器
			.failureHandler(yeaxuAuthenticationFailureHandler)
			.and()
//		http.httpBasic()  //启用httpBasic登录
			.authorizeRequests()
			.antMatchers("/authentication/require",
					"/code/image",
					securityProperties.getBrowser().getLoginPage()).permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf()
			.disable();
	}
}
