package com.smartcampus.security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.security.jwt.JwtService;
import com.smartcampus.security.model.ActivateModel;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.model.LoginModel;
import com.smartcampus.security.service.UserService;

@RestController
@RequestMapping("/api/v1/university")
@CrossOrigin("*")
public class UserRestController {
	@Autowired
	private UserService service;
	@Autowired
	private JwtService jwtService;

	@PostMapping("/auth/register")
	public ResponseEntity<?> createUser(@RequestBody CustomUserDetails user) {
		try {
			CustomUserDetails result = service.register(user);
			System.out.println("User::"+result.getId());
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(Map.of("message", "User created successfully", "instruction", "Check your mail::"+result.getEmail()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to create user: " + e.getMessage()));
		}
	}

	@PostMapping("/auth/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginModel loginInfo) {
		boolean loginSuccessful = service.login(loginInfo);
		Map<String, String> response = new HashMap<>();

		if (loginSuccessful) {
			response.put("message", "Login Successful😇");
			response.put("token", jwtService.generateToken(loginInfo.getUserId()));
			return ResponseEntity.ok(response);
		} else {
			response.put("error", "Invalid credentials");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}

	@PutMapping("/auth/update")
	public ResponseEntity<?> activate(@RequestBody ActivateModel activateModel) {
		String updateInfo = service.updatePassword(activateModel.getAcademicId(), activateModel.getPreviousPassword(),
				activateModel.getNewPassword());
		return ResponseEntity.ok(Map.of("message", updateInfo));
	}

	@GetMapping("/test")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_STAFF', 'ROLE_ADMIN', 'ROLE_BOARD_MEMBER', 'ROLE_DEVELOPER')")
	public ResponseEntity<?> testSecurity() {
	    return new ResponseEntity<>("Congratulations! Because you have a token 😇", HttpStatus.OK);
	}
	
	@PostMapping("/auth/forget")
	public ResponseEntity<?> forgetPassword(@RequestParam String userId){
		String forgetPassword = service.forgetPassword(userId);
		return new ResponseEntity<>(forgetPassword, HttpStatus.OK);
	}

}
