package com.yeaxu.security.demo.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yeaxu.security.demo.domain.User;

@RestController
public class UserController {
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public List<User> users(User user, @PageableDefault(page = 1) Pageable page/*@RequestParam(name = "username", required = false, defaultValue = "Tom") String nickname*/) {
		System.out.println(ReflectionToStringBuilder.toString(user));
//		System.out.println(nickname);
		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}
}
