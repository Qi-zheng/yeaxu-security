package com.yeaxu.security.browser.validate.code.impl;

import org.springframework.web.context.request.ServletWebRequest;

import com.yeaxu.security.core.validate.code.ValidateCode;
import com.yeaxu.security.core.validate.code.ValidateCodeRepository;
import com.yeaxu.security.core.validate.code.ValidateCodeType;

public class SessionValidateCodeRepository implements ValidateCodeRepository {

	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
		// TODO Auto-generated method stub

	}

}
