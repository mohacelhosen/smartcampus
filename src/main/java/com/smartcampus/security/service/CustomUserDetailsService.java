package com.smartcampus.security.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<CustomUserDetails> userInfo = repository.findByUserId(userId);
		logger.info("UserDetailsServiceImpl::loadUserByUsername, " + userInfo.toString());
		return userInfo.orElseThrow(() -> new UsernameNotFoundException("user not found " + userId));
	}

}
