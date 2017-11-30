package com.yeaxu.security.app.validate.code.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.ServletWebRequest;

import com.yeaxu.security.core.validate.code.ValidateCode;
import com.yeaxu.security.core.validate.code.ValidateCodeRepository;
import com.yeaxu.security.core.validate.code.ValidateCodeType;

public class RedisValidateCodeRepository implements ValidateCodeRepository {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
		redisTemplate.opsForValue().set(buildKey(request , validateCodeType), code, 30, TimeUnit.MINUTES);
	}

	private Object buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
		return null;
	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
		return null;
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
		redisTemplate.delete(buildKey(request, validateCodeType));
	}

}
