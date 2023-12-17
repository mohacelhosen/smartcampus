package com.smartcampus.department.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.course.model.Course;
import com.smartcampus.department.model.Department;
import com.smartcampus.department.service.DepartmentServie;
import com.smartcampus.exception.IncompleteDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university/department")
@CrossOrigin("*")
public class DepartmentController {
    @Autowired
    private DepartmentServie departmentServie;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Department>> register(@RequestBody Department department) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Department> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/register");

        try {
            Department registerDepartment = departmentServie.registerDepartment(department);

            response.setData(registerDepartment);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Department registered successfully");

            // Log successful registration
            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("CourseController::registerCourse, Successfully registered course.Timestamp: {}, Department ID: {}, Title: {}, ", time, department.getId(), department.getDepartmentName());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IncompleteDataException e) {
            // Log incomplete data exception
            logger.error("DepartmentController::register, Error registering department due to incomplete or duplicate data.Time:{}, Input Course: {}, Message: {}", time, department, e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Incomplete or duplicate data");
            response.setData(null);
        } catch (Exception ex) {
            // Log unexpected exceptions
            logger.error("DepartmentController::register, Unexpected error during department registration. Message: {}", ex.getMessage(), ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Internal Server Error");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            MDC.clear();
        }
    }
}
