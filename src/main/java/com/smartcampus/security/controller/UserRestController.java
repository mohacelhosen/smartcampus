package com.smartcampus.security.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.role.model.UserRole;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    @Autowired
    private UserService service;
    @Autowired
    private JwtService jwtService;

    @Hidden
    @PostMapping("/auth/register")
    public ResponseEntity<?> createUser(@RequestBody CustomUserDetails user) {
        try {
            CustomUserDetails result = service.register(user);
            logger.info("UserRestController:createUser, User DB ID::" + result.getId());
            logger.info("UserRestController:createUser, User_ID::" + result.getAcademicId());
            logger.info("UserRestController:createUser, User PWD::" + result.getPassword());
            logger.info("UserRestController:createUser, User PRE_PWD::" + Arrays.toString(result.getPreviousPassword().toArray()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User created successfully", "instruction", "Check your mail::" + result.getEmail()));
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

	@GetMapping("/add-new-role")
    public ResponseEntity<ApiResponse<CustomUserDetails>> addRole(@RequestParam String userEmail, @RequestParam String addNewRole) {
        ApiResponse<CustomUserDetails> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setEndpoint("/api/v1/university/add-new-role");
		CustomUserDetails customUserDetails = service.addRole(userEmail, addNewRole);
		response.setData(customUserDetails);
        response.setMessage("New Role successfully added");
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(time);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-user-by-email")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CustomUserDetails>> findUserByEmail(@RequestParam String userEmail) {
        ApiResponse<CustomUserDetails> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setEndpoint("/api/v1/university/find-user-by-email");
        CustomUserDetails userDetails = service.findByEmail(userEmail);
        response.setData(userDetails);
        response.setMessage("Successfully retrieve");
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(time);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_STAFF', 'ROLE_ADMIN', 'ROLE_BOARD_MEMBER', 'ROLE_DEVELOPER')")
    public ResponseEntity<?> testSecurity() {
        return new ResponseEntity<>("Congratulations! Because you have a token 😇", HttpStatus.OK);
    }

    @PostMapping("/auth/forget")
    public ResponseEntity<?> forgetPassword(@RequestParam String userId) {
        String forgetPassword = service.forgetPassword(userId);
        return new ResponseEntity<>(forgetPassword, HttpStatus.OK);
    }

}
