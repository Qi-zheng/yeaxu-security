package com.yeaxu.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.yeaxu.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.yeaxu.security.core.properties.SecurityProperties;
import com.yeaxu.security.core.validate.code.ValidateCodeFilter;
import com.yeaxu.security.core.validate.code.sms.SmsCodeFilter;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationSuccessHandler yeaxuAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository= new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setYeaxuAuthenticationFailureHandler(yeaxuAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setYeaxuAuthenticationFailureHandler(yeaxuAuthenticationFailureHandler);
		smsCodeFilter.setSecurityProperties(securityProperties);
		smsCodeFilter.afterPropertiesSet();
		
		http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin()//启用表单登录
			// /yeaxu-signIn.html  换成/authentication/require  一个url中处理 则可以让用户实现配置登录页的目地
			.loginPage("/authentication/require")  
			.loginProcessingUrl("/authentication/form") 
			.successHandler(yeaxuAuthenticationSuccessHandler)  //设置自定义成功处理器
			.failureHandler(yeaxuAuthenticationFailureHandler)
			.and()
			.rememberMe()
			.tokenRepository(persistentTokenRepository())
			.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
			.userDetailsService(userDetailsService)
			.and()
//		http.httpBasic()  //启用httpBasic登录
			.authorizeRequests()
			.antMatchers("/authentication/require",
					"/code/*",
					securityProperties.getBrowser().getLoginPage()).permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf()
			.disable()
			.apply(smsCodeAuthenticationSecurityConfig);
	}
}
