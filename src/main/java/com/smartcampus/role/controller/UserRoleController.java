package com.smartcampus.role.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.role.model.UserRole;
import com.smartcampus.role.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university/role")
@CrossOrigin("*")
public class UserRoleController {

    @Autowired
    private UserRoleService roleService;
    @PostMapping("/register-new-role")
    public ResponseEntity<ApiResponse<UserRole>> register(@RequestBody UserRole userRole){
        ApiResponse<UserRole> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setEndpoint("/api/v1/university/role/register-new-role");
        UserRole role = roleService.registerNewRole(userRole);
        response.setData(role);
        response.setMessage("Register new Role successfully");
        response.setStatus(HttpStatus.CREATED.value());
        response.setTimestamp(time);
        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<List<UserRole>>> getAllUserRoles(@RequestBody UserRole userRole){
        ApiResponse<List<UserRole>> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setEndpoint("/api/v1/university/role/register");
        List<UserRole> role = roleService.findAllRoles();
        response.setData(role);
        response.setMessage("Retrieve all Role successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(time);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
}
