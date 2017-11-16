package com.yeaxu.security.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.yeaxu.security.core.properties.SecurityProperties;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class) //ʹ���ö�ȡ����Ч
public class SecurityCoreConfig {

}
