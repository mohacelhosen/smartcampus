package com.smartcampus.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.service.UserService;

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserService userService;

	public Admin registerAdmin (Admin admin) {
		if (admin.getEmail() != null || !admin.getEmail().isEmpty()) {
			CustomUserDetails user = new CustomUserDetails();
			user.setAccountCreationDateTime(new ModelLocalDateTime(null));
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);
			user.setRole(admin.getRole());
			user.setUserId(admin.getUserId());
			user.setEmail(admin.getEmail());
			user.setEnabled(true);
			user.setFullName(admin.getFullName());

			CustomUserDetails register = userService.register(user);
			String password = register.getPassword();
			String randomPassword = register.getRandomPassword();
			admin.setPassword(password);
			admin.setRandomPassword(randomPassword);
			return adminRepository.save(admin);
		}else {
			throw new RuntimeException("Something went wrong");
		}
	}

}
