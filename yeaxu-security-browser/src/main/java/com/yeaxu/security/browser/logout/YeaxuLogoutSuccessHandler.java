package com.yeaxu.security.browser.logout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeaxu.security.core.support.SimpleResponse;

public class YeaxuLogoutSuccessHandler implements LogoutSuccessHandler {

	private String signOutUrl;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	public YeaxuLogoutSuccessHandler(String signOutUrl) {
		super();
		this.signOutUrl = signOutUrl;
	}


	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		if(StringUtils.isBlank(signOutUrl)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
		} else {
			response.sendRedirect(signOutUrl);
		}
	}

}
