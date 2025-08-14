package com.th.playnmovie.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.th.playnmovie.security.model.User;
import com.th.playnmovie.security.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("Attempting to load user by username: {}", username);
		
		User user = userRepository.findByUsername(username);
		if(user != null) {
			logger.info("User '{}' found and loaded.", username);
			return user;
		}
		else {
			logger.warn("User '{}' not found!", username);
			throw new UsernameNotFoundException("Username "+username+" not found!");
		}
		
	}
}
	
