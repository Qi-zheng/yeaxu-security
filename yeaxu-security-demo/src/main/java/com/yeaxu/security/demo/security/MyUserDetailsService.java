package com.yeaxu.security.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名：" + username);
		return buildUser(username);
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.info("社交登录用户名：" + userId);
		return buildUser(userId);
	}

	private SocialUserDetails buildUser(String userId) {
		//加了salt，同一个密码每次加密出来的串是不同的
		String password = passwordEncoder.encode("123456");
		logger.info("数据库密码是：" + password);
		return new SocialUser("Seven", password,
				//用户校验逻辑
				true, true, true, true, 
				AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN,ROLE_USER"));
	}

}
