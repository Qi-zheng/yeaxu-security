package com.yeaxu.security.browser.validate.code.impl;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.yeaxu.security.core.validate.code.ValidateCode;
import com.yeaxu.security.core.validate.code.ValidateCodeRepository;
import com.yeaxu.security.core.validate.code.ValidateCodeType;

@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

	/**
	 * 验证码放入session时的前缀
	 */
	private String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";
	
	/**
	 * 操作session的工具类
	 */
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Override
	public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
		sessionStrategy.setAttribute(request, getSessionKey(request), new ValidateCode(validateCode.getCode(), validateCode.getExpireTime()));
	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
		return (ValidateCode) sessionStrategy.getAttribute(request, getSessionKey(request));
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
		sessionStrategy.removeAttribute(request, getSessionKey(request));
	}
	
	/**
	 * 构建验证码放入session时的key
	 * 
	 * @param request
	 * @return
	 */
	private String getSessionKey(ServletWebRequest request) {
		return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
	}
	
	/**
	 * 根据请求的url获取校验码的类型
	 * 
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
		return ValidateCodeType.valueOf("IMAGE");
	}

}
