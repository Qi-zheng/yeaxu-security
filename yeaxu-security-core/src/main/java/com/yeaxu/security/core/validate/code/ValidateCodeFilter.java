package com.yeaxu.security.core.validate.code;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yeaxu.security.core.properties.SecurityConstants;
import com.yeaxu.security.core.properties.SecurityProperties;

/**
 * 实现InitializingBean接口是为了实现afterPropertiesSet方法，以便了在读取配置初始化完成后在调用该方法
 * @author seven
 *
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	@Autowired
	private AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	/**
	 * 系统配置信息
	 */
	@Autowired
	private SecurityProperties securityProperties;
	/**
	 * 系统中的校验码处理器
	 */
	@Autowired
	private ValidateCodeProcessorHolder validateCodeProcessorHolder;
	/**
	 * 存放所有需要校验验证码的url
	 */
	private Map<String, ValidateCodeType> urlMap = new HashMap<>();
	/**
	 * 验证请求url与配置的url是否匹配的工具类
	 */
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	/**
	 * 初始化要拦截的url配置信息
	 */
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
		addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

		urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
		addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
	}
	
	
	/**
	 * 讲系统中配置的需要校验验证码的URL根据校验的类型放入map
	 * 
	 * @param urlString
	 * @param type
	 */
	protected void addUrlToMap(String urlString, ValidateCodeType type) {
		if (StringUtils.isNotBlank(urlString)) {
			String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
			for (String url : urls) {
				urlMap.put(url, type);
			}
		}
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		ValidateCodeType type = getValidateCodeType(request);
		if (type != null) {
			logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
			try {
				validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(new ServletWebRequest(request, response));
				logger.info("验证码校验通过");
			} catch (ValidateCodeException exception) {
				//验证码验证不成功时的后续处理
				yeaxuAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);
				//验证码验证失败后不进行后面的验证操作比如：用户名密码等的验证
				return;
			}
		}
		//验证码验证成功时的后续处理
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 获取校验码的类型，如果当前请求不需要校验，则返回null
	 * 
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
		ValidateCodeType result = null;
		if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
			Set<String> urls = urlMap.keySet();
			for (String url : urls) {
				if (pathMatcher.match(url, request.getRequestURI())) {
					result = urlMap.get(url);
				}
			}
		}
		return result;
	}

	public AuthenticationFailureHandler getYeaxuAuthenticationFailureHandler() {
		return yeaxuAuthenticationFailureHandler;
	}

	public void setYeaxuAuthenticationFailureHandler(AuthenticationFailureHandler yeaxuAuthenticationFailureHandler) {
		this.yeaxuAuthenticationFailureHandler = yeaxuAuthenticationFailureHandler;
	}

//	public SecurityProperties getSecurityProperties() {
//		return securityProperties;
//	}
//
//	public void setSecurityProperties(SecurityProperties securityProperties) {
//		this.securityProperties = securityProperties;
//	}
//
//	public ValidateCodeProcessorHolder getValidateCodeProcessorHolder() {
//		return validateCodeProcessorHolder;
//	}
//
//	public void setValidateCodeProcessorHolder(ValidateCodeProcessorHolder validateCodeProcessorHolder) {
//		this.validateCodeProcessorHolder = validateCodeProcessorHolder;
//	}
//	
}
