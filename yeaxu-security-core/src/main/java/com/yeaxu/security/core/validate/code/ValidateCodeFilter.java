package com.yeaxu.security.core.validate.code;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

public class ValidateCodeFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationFailureHandler yeaxuAuthenticationFailureHandler;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(StringUtils.equals("/authentication/form", request.getRequestURI())
				&& StringUtils.equalsIgnoreCase(request.getMethod(),"post")) {
			
			try {
				validate(new ServletWebRequest(request));
			} catch (ValidateCodeException e) {
				yeaxuAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
			}
			
		} 
		
		filterChain.doFilter(request, response);

	}

	private void validate(ServletWebRequest servletWebRequest) {
		// TODO Auto-generated method stub
		
	}

}
