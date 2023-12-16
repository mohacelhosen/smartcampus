package com.smartcampus.admin.controller;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university")
@CrossOrigin("*")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/admin/register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Admin> register(@RequestBody Admin admin){
        Admin registerAdmin = adminService.registerAdmin(admin);
        return new ResponseEntity<>(registerAdmin, HttpStatus.CREATED);
    }
    @PostMapping("/admin/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Admin> update(@RequestBody Admin admin){
        Admin registerAdmin = adminService.updateAdminInfo(admin);
        return new ResponseEntity<>(registerAdmin, HttpStatus.OK);
    }
}
