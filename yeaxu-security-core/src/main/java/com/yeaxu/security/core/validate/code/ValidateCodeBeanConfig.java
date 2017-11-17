package com.yeaxu.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yeaxu.security.core.properties.SecurityProperties;
import com.yeaxu.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.yeaxu.security.core.validate.code.sms.SmsCodeSender;

@Configuration
public class ValidateCodeBeanConfig {
	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	@ConditionalOnMissingBean(name = "imageCodeGenerator")  // 当容器中不存在名字叫做imageCodeGenerator的Bean时注册以下的默认的Bean
	public ValidateCodeGenerator imageCodeGenerator() {
		ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
		imageCodeGenerator.setSecurityProperties(securityProperties);
		return imageCodeGenerator;
	}
	
	@Bean
	@ConditionalOnMissingBean(SmsCodeSender.class)  // 效果同图片生成器注解
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsCodeSender();
	}
}
