package com.yeaxu.security.core.social.qq.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.web.servlet.View;

import com.yeaxu.security.core.properties.QQProperties;
import com.yeaxu.security.core.properties.SecurityProperties;
import com.yeaxu.security.core.social.YeaxuConnectionBandingView;
import com.yeaxu.security.core.social.qq.connet.QQConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "yeaxu.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private DataSource dataSource;

	@Autowired(required = false)
	private ConnectionSignUp connectionSignUp;

	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqConfig = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
	}

	/**
	 * 如果不写在这里面就会出现Spring注入InMemoryUsersConnectionRepository有情况 
	 * //	@Bean
	 *	//	public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
	 *	//		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
	 *	//		repository.setTablePrefix("yeaxu_");
	 *	//		if(connectionSignUp != null) {
	 *	//			repository.setConnectionSignUp(connectionSignUp);
	 *	//		}
	 *	//		return repository;
	 *	//	}  可解决
	 */
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		repository.setTablePrefix("yeaxu_");
		if (connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}
	
	@Bean({"connect/qqConnected", "connect/qqConnect"})
	@ConditionalOnMissingBean(name = "qqBandingView")
	public View qqBandingView() {
		return new YeaxuConnectionBandingView();
	}
	
}
