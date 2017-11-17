package com.yeaxu.security.core.validate.code;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yeaxu.security.core.properties.SecurityProperties;

/**
 * 实现InitializingBean接口是为了实现afterPropertiesSet方法，以便了在读取配置初始化完成后在调用该方法
 * @author seven
 *
 */
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	@Autowired
	private AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	private Set<String> urls = new HashSet<>();
	
	private SecurityProperties securityProperties;
	
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",");
		for (String configUrl : configUrls) {
			urls.add(configUrl);
		}
		urls.add("/authentication/form");
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		boolean action = false;
		for (String url : urls) {
			if (antPathMatcher.match(url, request.getRequestURI())) {
				action = true;
			}
		}
		if(action) {
			try {
				validate(new ServletWebRequest(request));
			} catch (ValidateCodeException e) {
				//验证码验证不成功时的后续处理
				yeaxuAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
				//验证码验证失败后不进行后面的验证操作比如：用户名密码等的验证
				return;
			}
		}
		//验证码验证成功时的后续处理
		filterChain.doFilter(request, response);
	}

	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeController.SESSION_KEY);
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
		
		if(StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("验证码的值不能为空");
		}
		
		if(codeInSession == null ) {
			throw new ValidateCodeException("验证码不存在");
		}
		
		if(codeInSession.isExpried()) {
			sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
			throw new ValidateCodeException("验证码已过期");
		}
		
		if(!StringUtils.equals(codeInRequest, codeInSession.getCode())) {
			throw new ValidateCodeException("验证码不匹配");
		}
		sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
	}

	public AuthenticationFailureHandler getYeaxuAuthenticationFailureHandler() {
		return yeaxuAuthenticationFailureHandler;
	}

	public void setYeaxuAuthenticationFailureHandler(AuthenticationFailureHandler yeaxuAuthenticationFailureHandler) {
		this.yeaxuAuthenticationFailureHandler = yeaxuAuthenticationFailureHandler;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
	
	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
	
}
