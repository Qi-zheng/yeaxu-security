package com.yeaxu.security.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("��¼�û�����" + username);
		//����salt��ͬһ������ÿ�μ��ܳ����Ĵ��ǲ�ͬ��
		String password = passwordEncoder.encode("123456");
		logger.info("���ݿ������ǣ�" + password);
		return new User("Seven", password,
				//�û�У���߼�
				true, true, true, true, 
				AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
	}

}
