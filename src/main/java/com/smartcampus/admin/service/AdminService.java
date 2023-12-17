package com.smartcampus.admin.service;

import com.smartcampus.common.CustomUserObj;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.security.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.repository.AdminRepository;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*
{
  "userId": "string",
  "email": "string",
  "fullName": "string",
  "role": "string"
}
 */
@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;

	public Admin registerAdmin(Admin admin) {
		if (admin.getEmail() != null && !admin.getEmail().isEmpty()) {
			int nextId = 102;
			List<String> adminEmailList = new ArrayList<>();
			adminEmailList.add(admin.getEmail());
			List<Admin> adminList = adminRepository.findAll();
			if (!adminList.isEmpty()) {
				Admin lastAdmin = adminList.get(adminList.size() - 1);
				String lastAdminId = lastAdmin.getUserId();
				int lastId = Integer.parseInt(lastAdminId);
				nextId = lastId + 1;
				adminList.forEach(x -> adminEmailList.add(x.getEmail()));
			}

			// Creating a CustomUserDetails instance
			CustomUserDetails user = CustomUserObj.customObj(
					String.valueOf(nextId),
					admin.getEmail(),
					admin.getFullName(),
					admin.getRole()
			);

			// Registering the user
			CustomUserDetails registeredUser = userService.register(user);

			// Getting the generated password from the registered user
			String password = registeredUser.getPassword();

			// Setting admin details
			admin.setUserId(String.valueOf(nextId));
			admin.setPassword(password);
			admin.setId(registeredUser.getId());
			admin.setCreationTime(new ModelLocalDateTime(null).getLocalDateTimeStringAMPM());

			// Saving the admin
			Admin save = adminRepository.save(admin);
			MailDto dto = new MailDto();
			dto.setTo(adminEmailList);
			dto.setSubject("Account Credential");
			dto.setHtmlString("Full Name::"+save.getFullName()+", Email::"+save.getEmail());
			dto.setTextBody("User Account Credentials");
			emailService.sendEmailWithAttachment(dto);
			return save;
		} else {
			throw new RuntimeException("Email cannot be null or empty");
		}
	}


	public Admin updateAdminInfo (Admin admin) {
		if (admin.getEmail() != null || !admin.getEmail().isEmpty()) {
			Optional<Admin> dbUserOptional = adminRepository.findByUserId(admin.getUserId());
			if (dbUserOptional.isEmpty()){
				throw new RuntimeException("Admin(MDB) id not found");
			}
			Admin dbAdmin = dbUserOptional.get();
			Optional<CustomUserDetails> customUserOptional = userRepository.findByUserId(dbAdmin.getUserId());
			if (customUserOptional.isEmpty()){
				throw new RuntimeException("Admin(SDB) id not found");
			}
			CustomUserDetails dbCustomUser = customUserOptional.get();
			dbCustomUser.setFullName(admin.getFullName());
			dbCustomUser.setEmail(admin.getEmail());
			userRepository.save(dbCustomUser);
			dbAdmin.setFullName(admin.getFullName());
			dbAdmin.setEmail(admin.getEmail());
			return adminRepository.save(dbAdmin);
		}else {
			throw new RuntimeException("Something went wrong");
		}
	}

	public List<Admin> allAdmin(){
		return adminRepository.findAll();
	}

}
