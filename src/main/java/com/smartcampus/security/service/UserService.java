package com.smartcampus.security.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smartcampus.common.GeneralConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RandomPasswordGenerator;
import com.smartcampus.email.dto.MailDto;
import com.smartcampus.email.service.EmailService;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.model.LoginModel;
import com.smartcampus.security.repository.UserRepository;


@Service
public class UserService {

	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	// <------------------------ Register ----------------->
	public CustomUserDetails register(CustomUserDetails user) {
		String email = user.getEmail();
		boolean validEmail = isValidEmail(email);
		if (email != null && validEmail) {
			Optional<CustomUserDetails> dbUser = repository.findUserByEmail(email);
			if (dbUser.isPresent()) {
				throw new AlreadyExistsException("Already Registered");
			}

			Optional<CustomUserDetails> dbUserById = repository.findByUserId(user.getUserId());
			if (dbUserById.isPresent()) {
				throw new AlreadyExistsException("Already Registered");
			} else {
				// Generate a random password
				String password = RandomPasswordGenerator.generateRandomPassword(15);
				String encodePWD = passwordEncoder.encode(password);

				// Set the generated password for the user
				user.setPassword(encodePWD);
				user.setPreviousPassword(Collections.singletonList(encodePWD));
				user.setAccountCreationDateTime(new ModelLocalDateTime(null));
				if (user.getRole().equalsIgnoreCase("Teacher")) {
					user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_TEACHER));
				} else if (user.getRole().equalsIgnoreCase("Student")) {
					user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_STUDENT));
				} else if (user.getRole().equalsIgnoreCase("Staff")) {
					user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_STAFF));
				} else if (user.getRole().equalsIgnoreCase("Admin")) {
					user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_ADMIN));
				} else if (user.getRole().equalsIgnoreCase("BoardMember")) {
					user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_BOARD_MEMBER));
				} else {
					user.setAuthorities(Collections.singletonList(GeneralConstants.ROLE_GUEST));
				}
				user.setAccountCreationDateTime(new ModelLocalDateTime(null));
				CustomUserDetails savedUser = repository.save(user);

				sendMail(user.getEmail(), user.getUsername(), user.getUserId(), password);

				// Return the saved user
				return savedUser;
			}
		} else {
			throw new NotFoundException("Email id can't be null or duplicate or invalid email");
		}
	}

	// <------------------------ Login ------------------------->
	public boolean login(LoginModel loginInfo) {
		if (isInvalidLoginInfo(loginInfo)) {
			logger.error("Incomplete user info provided for login");
			return false;
		}

		logger.info("UserService::login, ❓loginInfo found::{}", true);

		String userId = loginInfo.getUserId();
		Optional<CustomUserDetails> userEntity = getUserById(userId);

		if (userEntity.isPresent()) {
			if (!userEntity.get().isEnabled()) {
				logger.info("UserService::login, ❓account is not enabled::{}", false);
				return false; // Account is not enabled
			}

			logger.info("UserService::login, ❓loginInfo exists::{}", true);
			boolean passwordMatches = passwordEncoder.matches(loginInfo.getPassword(), userEntity.get().getPassword());

			if (passwordMatches) {
				logger.info("UserService::login, ❓logging pwd match::{}", true);
				return true; // Successful login
			} else {
				logger.info("UserService::login, ❓logging pwd match::{}", false);
				return false; // Incorrect password
			}
		} else {
			logger.info("UserServiceImpl::login, ❓loginInfo exists::{}", false);
			return false; // User not found
		}
	}

	private boolean isInvalidLoginInfo(LoginModel loginInfo) {
		return loginInfo == null || loginInfo.getUserId() == null || loginInfo.getUserId().isEmpty()
				|| loginInfo.getPassword() == null || loginInfo.getPassword().isEmpty();
	}

	private Optional<CustomUserDetails> getUserById(String userId) {
		return repository.findByUserId(userId);
	}

	public String updatePassword(String userId, String previousPassword, String newPassword) {
		Optional<CustomUserDetails> userOptional = repository.findByUserId(userId);

		if (userOptional.isPresent()) {
			CustomUserDetails user = userOptional.get();
			boolean passwordMatches = passwordEncoder.matches(previousPassword, user.getPassword());

			if (passwordMatches) {
				// Update the password fields
				String encodePWD = passwordEncoder.encode(newPassword);
				user.setPassword(encodePWD);
				List<String> previousPwd = user.getPreviousPassword();
				previousPwd.add(encodePWD);
				user.setPreviousPassword(previousPwd);
				user.setEnabled(true);
				user.setLastPasswordResetDate(new ModelLocalDateTime(null));

				// Save the updated user details
				CustomUserDetails userDetails = repository.save(user);

				// Send notification email
				sendMail(user.getEmail(), user.getUsername(), user.getUserId(), newPassword);
				logger.info("UserService:updatePassword, Total Previous PWD::"+ Arrays.toString(userDetails.getPreviousPassword().toArray()));

				return "Successfully updated";
			} else {
				return "Incorrect previous password";
			}
		} else {
			return "User not found";
		}
	}

	public String forgetPassword(String userId) {
		Optional<CustomUserDetails> userOptional = repository.findByUserId(userId);

		if (userOptional.isPresent()) {
			CustomUserDetails user = userOptional.get();

			// Generate a sufficiently secure random password
			String generateRandomPassword = RandomPasswordGenerator.generateRandomPassword(25);

			String encodePWD = passwordEncoder.encode(generateRandomPassword);
			user.setPassword(encodePWD);
			// Update the user's password fields
			user.setPassword(passwordEncoder.encode(generateRandomPassword));
			List<String> previousPwd = user.getPreviousPassword();
			previousPwd.add(encodePWD);
			user.setPreviousPassword(previousPwd);
			user.setEnabled(true);
			user.setLastPasswordResetDate(new ModelLocalDateTime(null));

			// Save the updated user details
			repository.save(user);

			// Send a notification email with the new password
			sendMail(user.getEmail(), user.getUsername(), user.getUserId(), generateRandomPassword);

			return "Successfully updated";
		} else {
			return "User not found";
		}
	}

	public String disableAccount(String userId) {
		Optional<CustomUserDetails> user = repository.findByUserId(userId);
		if (user.isPresent()) {
			user.get().setEnabled(false);
			repository.save(user.get());
			return "Account disabled successfully";
		}
		return "User not found";
	}

	public String enableAccount(String userId) {
		Optional<CustomUserDetails> user = repository.findByUserId(userId);
		if (user.isPresent()) {
			user.get().setEnabled(true);
			repository.save(user.get());
			return "Account enabled successfully"; // Corrected the return message
		}
		return "User not found";
	}

	public String accountLocked(String userId) {
		Optional<CustomUserDetails> user = repository.findByUserId(userId);
		if (user.isPresent()) {
			user.get().setAccountNonLocked(false); // Set accountNonLocked to false to lock the account
			repository.save(user.get());
			return "Account locked successfully";
		}
		return "User not found";
	}

	public String accountUnLocked(String userId) {
		Optional<CustomUserDetails> user = repository.findByUserId(userId);
		if (user.isPresent()) {
			user.get().setAccountNonLocked(true); // Set accountNonLocked to true to unlock the account
			repository.save(user.get());
			return "Account unlocked successfully";
		}
		return "User not found";
	}

	public String accountExpired(String userId) {
		Optional<CustomUserDetails> user = repository.findByUserId(userId);
		if (user.isPresent()) {
			user.get().setAccountNonExpired(false); // Set accountNonExpired to false to expire the account
			repository.save(user.get());
			return "Account expired";
		}
		return "User not found";
	}

	public String accountRenew(String userId) {
		Optional<CustomUserDetails> user = repository.findByUserId(userId);
		if (user.isPresent()) {
			user.get().setAccountNonExpired(true); // Set accountNonExpired to true to renew the account
			repository.save(user.get());
			return "Account renewed successfully";
		}
		return "User not found";
	}

	private void sendMail(String email, String userName, String userId, String password) {
		// Read email template file content
		String fileContent = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "    <style>\r\n"
				+ "        body {\r\n" + "            margin: 0;\r\n" + "            padding: 0;\r\n"
				+ "            display: flex;\r\n" + "            justify-content: center;\r\n"
				+ "            align-items: center;\r\n" + "            font-family: 'Arial', sans-serif;\r\n"
				+ "            background-color: #f4f4f4;\r\n" + "        }\r\n" + "\r\n" + "    .container {\r\n"
				+ "    background-image: url(https://img.freepik.com/free-photo/3d-render-pen-with-notebook_23-2150800905.jpg?t=st=1700529544~exp=1700533144~hmac=d76d5dc3e3862338d2ccefd0498dc838e51f87271bf35eece13aafe16f5387f7&w=740);\r\n"
				+ "    background-position: center;\r\n" + "    background-size: contain;\r\n"
				+ "    background-repeat: no-repeat;\r\n" + "    max-width: 640px;\r\n" + "    margin: 0 auto;\r\n"
				+ "    margin-top: 15px;\r\n" + "    padding: 20px;\r\n" + "    padding-bottom: 0px;\r\n"
				+ "    border-radius: 5px;\r\n" + "    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\r\n"
				+ "    overflow: hidden;\r\n" + "    background-color: #fff;\r\n" + "    position: relative;\r\n"
				+ "    }\r\n" + "\r\n" + ".container::after {\r\n" + "    content: \"\";\r\n"
				+ "    position: absolute;\r\n" + "    top: 0;\r\n" + "    left: 0;\r\n" + "    right: 0;\r\n"
				+ "    bottom: 0;\r\n" + "    background-color: rgba(7, 7, 7, 0.17);\r\n" + "}\r\n" + "\r\n" + "\r\n"
				+ "        h2 {\r\n" + "            color: #333;\r\n" + "        }\r\n" + "\r\n" + "        p {\r\n"
				+ "            font-size: 16px;\r\n" + "            line-height: 1.6;\r\n" + "        }\r\n" + "\r\n"
				+ "        .button {\r\n" + "            display: inline-block;\r\n"
				+ "            padding: 10px 20px;\r\n" + "            background-color: #007BFF;\r\n"
				+ "            color: #fff;\r\n" + "            text-decoration: none;\r\n"
				+ "            border-radius: 5px;\r\n" + "        }\r\n" + "\r\n" + "        .button:hover {\r\n"
				+ "            background-color: #0056b3;\r\n" + "        }\r\n" + "    </style>\r\n" + "</head>\r\n"
				+ "<body>\r\n" + "<table >\r\n" + "    \r\n" + "    <tr>\r\n" + "        <td>\r\n"
				+ "            <div class=\"container\">\r\n"
				+ "                <div style=\"width: 110%; margin-top: -20px; margin-left: -26px; height: 16px; background: #214689;\"></div>\r\n"
				+ "                <h2>Account Credential</h2>\r\n" + "                <p>Dear #user#,</p>\r\n"
				+ "                <p>We hope this message finds you well. We kindly request you to change your password first password generated by system, but if you updated then plz delete this message</p>\r\n"
				+ "                <p>UserId: <b>#userId#</b></p>\r\n"
				+ "                <p>Password: <b>#password#</b></p>\r\n"
				+ "                <p>Your prompt attention to this matter is greatly appreciated, and it will help streamline the admission process.</p>\r\n"
				+ "                <p>If you have any questions or need further information, please feel free to contact </p>\r\n"
				+ "                <p>Thank you for your cooperation.</p>\r\n"
				+ "                <div style=\"width: 110%; margin-left: -26px; height: 16px; background: #214689;\"></div>\r\n"
				+ "            </div>\r\n" + "        </td>\r\n" + "    </tr>\r\n" + "</table>\r\n" + "</body>\r\n"
				+ "</html>\r\n" + "";

		if (fileContent != null) {
			// Replace placeholders in the email template
			String modifiedContent = fileContent.replace("#user#", userName).replace("#userId#", userId)
					.replace("#password#", password);

			// Send an email with user credentials
			MailDto dto = new MailDto();
			dto.setTo(Collections.singletonList(email));
			dto.setSubject("Account Credential");
			dto.setHtmlString(modifiedContent);
			dto.setTextBody("User Account Credentials");
			emailService.sendEmailWithAttachment(dto);
			System.out.println("Mail sent::" + email);
		}

	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^(?=.{1,255}$)(?=.{1,64}@.{1,255}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*){1,}[a-zA-Z]{2,7}$";

		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}
}
