package com.security.oauth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.security.oauth.model.User;

@Service
public class UserService {

	public List<User> getAllUsers() throws Exception{
		
		List<User> users = new ArrayList<>();
		users.add(new User(1,"imran",25,23));
		users.add(new User(1,"imran",25,23));
		users.add(new User(1,"imran",25,23));
		
		return users;
	}
}
