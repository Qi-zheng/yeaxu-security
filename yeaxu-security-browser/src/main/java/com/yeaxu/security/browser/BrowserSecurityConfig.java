package com.yeaxu.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

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
	

	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
	
	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

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
					.invalidSessionStrategy(invalidSessionStrategy)  //当session不可用时的策略
					.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())  //同一个用户最大的登录数
					.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())  //是否保护登录，只允许一个用户登录
					.expiredSessionStrategy(sessionInformationExpiredStrategy)  //当session因并发间登录失效时的策略
			.and()
			.and()
				.logout()
					.logoutUrl("/signOut")
//					.logoutSuccessUrl("/yeaxu-signOut.html")  //不能和logoutSuccessHandler同时使用
					.logoutSuccessHandler(logoutSuccessHandler)
					.deleteCookies("JSESSIONID")   //如果不配置这一项，则会出现到了yeaxu-signOut.html页面后检查session,出现跳转session过期页面
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
