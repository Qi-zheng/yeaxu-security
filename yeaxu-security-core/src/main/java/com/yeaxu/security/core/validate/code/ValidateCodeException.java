package com.yeaxu.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7427690997104693244L;

	public ValidateCodeException(String msg) {
		super(msg);
	}

	

}
