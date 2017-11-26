package com.yeaxu.security.core.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

import com.yeaxu.security.core.properties.QQProperties;
import com.yeaxu.security.core.properties.SecurityProperties;
import com.yeaxu.security.core.social.qq.connet.QQConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "yeaxu.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqProperties = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqProperties.getProviderId(), qqProperties.getAppId(), qqProperties.getAppSecret());
	}

}
