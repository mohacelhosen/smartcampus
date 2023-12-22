package com.smartcampus.department.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.course.model.Course;
import com.smartcampus.department.model.Department;
import com.smartcampus.department.service.DepartmentServie;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            Department registerDepartment = departmentServie.registerDepartment(department, department.getTotalSemester());

            response.setData(registerDepartment);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Department registered successfully");

            // Log successful registration
            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("DepartmentController::register, Successfully registered course.Timestamp: {}, Department ID: {}, Title: {}, ", time, department.getId(), department.getDepartmentName());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IncompleteDataException e) {
            // Log incomplete data exception
            logger.error("DepartmentController::register, Error registering department due to incomplete or duplicate data.Time:{}, Input Course: {}, Message: {}", time, department, e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Incomplete or duplicate data");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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

    @PutMapping("/update-info")
    public ResponseEntity<ApiResponse<Department>> updateDepartmentInfo(@RequestParam String departmentCode, @RequestBody Department department) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Department> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/update-info");
        try {
            Department departmentInfo = departmentServie.updateDepartmentInfo(departmentCode, department);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(" Successfully update department information");
            response.setData(departmentInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("DepartmentController::updateDepartmentInfo, Error updating the course information due to Invalid Department code.Timestamp:{}, Department Code:{}, Input Department: {}, Message: {}", time, departmentCode, department, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/add-course")
    public ResponseEntity<ApiResponse<Department>> addCourseInDepartment(@RequestParam String departmentCode, @RequestParam String courseCode, @RequestParam String institutionCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Department> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/add-course");
        try {
            Department departmentInfo = departmentServie.addCourseInDepartment(departmentCode, courseCode,institutionCode);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(" Successfully added course in department");
            response.setData(departmentInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("DepartmentController::updateDepartmentInfo, Error updating the Department information due to Invalid department Code.Department Code:{},  Input Course: {}, Message: {}", departmentCode, courseCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-by-department-code")
    public ResponseEntity<ApiResponse<Department>> findDepartmentByCode(@RequestParam String departmentCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Department> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/find-by-department-code");
        try {
            Department departmentInfo = departmentServie.findByDepartmentCode(departmentCode);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(" Successfully retriv  department");
            response.setData(departmentInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("DepartmentController::findDepartmentByCode, Error updating the Department information due to Invalid department Code.Department Code:{}, Message: {}", departmentCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-by-department-id")
    public ResponseEntity<ApiResponse<Department>> findDepartmentById(@RequestParam String departmentId) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Department> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/find-by-department-id");
        try {
            Department departmentInfo = departmentServie.findByDepartmentId(departmentId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(" Successfully retriv  department");
            response.setData(departmentInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("DepartmentController::findDepartmentById, Error updating the Department information due to Invalid department Code. Timestamp:{}, Department Code:{}, Message: {}", time, departmentId, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-all-department")
    public ResponseEntity<ApiResponse<List<Department>>> findAllDepartment() {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<List<Department>> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/find-all-department");

        List<Department> allDepartment = departmentServie.findAllDepartment();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(" Successfully retriv  department");
        response.setData(allDepartment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/remove-course-by-course-id")
    public ResponseEntity<ApiResponse<Department>> removeCourseByCourseId(@RequestParam String departmentCode, @RequestParam String courseCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Department> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/department/remove-course-by-course-id");
        try {
            Department department = departmentServie.removeCourseByCourseId( departmentCode,  courseCode);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(" Successfully retriv  department");
            response.setData(department);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("DepartmentController::removeCourseByCourseId, Error to remove course due to Invalid department Code . Timestamp:{},  Message: {}", time, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
