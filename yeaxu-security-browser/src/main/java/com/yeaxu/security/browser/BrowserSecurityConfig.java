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
		http.formLogin()//���ñ���¼
			// /yeaxu-signIn.html  ����/authentication/require  һ��url�д��� ��������û�ʵ�����õ�¼ҳ��Ŀ��
			.loginPage("/authentication/require")  
			.loginProcessingUrl("/authentication/form") 
			.successHandler(yeaxuAuthenticationSuccessHandler)  //�����Զ���ɹ�������
			.failureHandler(yeaxuAuthenticationFailureHandler)
			.and()
//		http.httpBasic()  //����httpBasic��¼
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
