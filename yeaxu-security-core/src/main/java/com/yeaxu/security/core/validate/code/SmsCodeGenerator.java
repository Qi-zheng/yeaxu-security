package com.yeaxu.security.core.validate.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.yeaxu.security.core.properties.SecurityProperties;

@Component
public class SmsCodeGenerator implements ValidateCodeGenerator {
	
	private SecurityProperties securityProperties;

	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
		return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
	}
	
	
	
	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}



	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
