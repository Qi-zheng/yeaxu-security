package com.yeaxu.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import com.yeaxu.security.browser.session.YeaxuExpiredSessionStrategy;
import com.yeaxu.security.core.authentication.AbstractChannelSecurityConfig;
import com.yeaxu.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.yeaxu.security.core.properties.SecurityConstants;
import com.yeaxu.security.core.properties.SecurityProperties;
import com.yeaxu.security.core.validate.code.ValidateCodeSecurityConfig;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private SpringSocialConfigurer yeaxuSocialSecurityConfig;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		// tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		applyPasswordAuthenticationConfig(http);
		http.apply(validateCodeSecurityConfig)
			.and()
			.apply(smsCodeAuthenticationSecurityConfig)
			.and()
			.apply(yeaxuSocialSecurityConfig)
			.and()
			.rememberMe()
			.tokenRepository(persistentTokenRepository())
			.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
			.userDetailsService(userDetailsService)
			.and()
			.sessionManagement()
				.invalidSessionUrl("/session/invalid")
				.maximumSessions(1)
				.maxSessionsPreventsLogin(true)  //是否保护登录，只允许一个用户登录
				.expiredSessionStrategy(new YeaxuExpiredSessionStrategy())
				.and()
			.and()
			.authorizeRequests()
			.antMatchers(
				SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
				securityProperties.getBrowser().getLoginPage(),
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
				"/user/regist", "/session/invalid", securityProperties.getBrowser().getSignUpUrl())
				.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf().disable();
			
	}

}
