package com.smartcampus.admin.controller;

import com.smartcampus.admin.model.Admin;
import com.smartcampus.admin.model.TeacherApprove;
import com.smartcampus.admin.service.AdminService;
import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.teacher.model.Teacher;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university")
@CrossOrigin("*")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
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

    @PostMapping("/admin/approve-teacher")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Teacher>> approveTeacher(@RequestBody TeacherApprove teacherApprove){
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Teacher> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/admin/approve-teacher");
        try{
            Teacher teacher = adminService.teacherInformationCheckAndApprove(teacherApprove);
            response.setData(teacher);
            response.setStatus(HttpStatus.OK.value());
            // Log successful registration
            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("AdminController::approveTeacher, Successfully Approve Teacher .Timestamp: {}, teacher ID:{}", time, teacher.getTeacherId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (NotFoundException e){
            response.setData(null);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            logger.error("AdminController::approveTeacher, Teacher not found .Timestamp:{}, Input: {}, Message: {}", time, teacherApprove, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

}
