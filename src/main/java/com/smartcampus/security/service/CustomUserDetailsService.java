package com.smartcampus.security.service;

import java.util.Arrays;
import java.util.Optional;

import com.smartcampus.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartcampus.security.model.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<CustomUserDetails> userInfo = userRepository.findByUserId(userId);
		logger.info("CustomUserDetailsService::loadUserByUsername, Full Name:{},  Role:{}, Authority:{}", userInfo.get().getFullName(), userInfo.get().getRole(), Arrays.toString(userInfo.get().getAuthorities().toArray()));
		return userInfo.orElseThrow(() -> new UsernameNotFoundException("user not found " + userId));
	}

}
