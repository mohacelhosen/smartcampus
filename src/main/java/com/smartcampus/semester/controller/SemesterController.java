package com.smartcampus.semester.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.semester.model.Semester;
import com.smartcampus.semester.service.SemesterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university/semester")
@CrossOrigin("*")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;
    private static final Logger logger = LoggerFactory.getLogger(SemesterController.class);

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Semester>> register(@RequestBody Semester semester) {
        ApiResponse<Semester> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/semester/register");
        try {
            Semester registerSemester = semesterService.registerSemester(semester);
            response.setData(registerSemester);
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("SemesterController::register, semester register successfully. Timestamp:{}, ",time);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("SemesterController::register, Semester register fail. Timestamp:{},  Input:{}, Message:{}",time, semester, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/add-course-teacher-class")
    public ResponseEntity<ApiResponse<Semester>> addCourseTeacherClass(@RequestParam String semesterCode, @RequestParam String courseCode, @RequestParam String teacherRegistrationId, @RequestParam String classJoinCode, @RequestParam String institutionCode) {
        ApiResponse<Semester> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/semester/add-course-teacher-class");
        try {
            Semester registerSemester = semesterService.addCourseAndTeacherAndClass( semesterCode,  courseCode,  teacherRegistrationId,  classJoinCode, institutionCode);
            response.setData(registerSemester);
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("SemesterController::addCourseTeacherClass, successfully added Course Teacher and  Class in Semester. Timestamp:{}, ",time);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("SemesterController::addCourseTeacherClass,  fail  to add Course Teacher and  Class  in semester. Timestamp:{},  semesterCode:{}, courseCode:{}, teacherId:{}, classJoinCode:{}, Message:{}",time, semesterCode,courseCode,teacherRegistrationId,classJoinCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-semester-by-code")
    public ResponseEntity<ApiResponse<Semester>> getSemesterByCode(@RequestParam String semesterCode, @RequestParam String institutionCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        try {
            Semester semester = semesterService.findBySemesterCode(semesterCode, institutionCode);
            ApiResponse<Semester> response = new ApiResponse<>(200, "Semester retrieved successfully", semester,time, "/api/v1/university/semester/get-semester-by-code");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ApiResponse<Semester> response = new ApiResponse<>(400, e.getMessage(), null,time, "/api/v1/university/semester/get-semester-by-code");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-all-semester")
    public ResponseEntity<ApiResponse<List<Semester>>> getAllSemester() {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        try {
            List<Semester> semester = semesterService.findAllSemester();
            ApiResponse<List<Semester>> response = new ApiResponse<>(200, "Semester retrieved successfully", semester,time, "/api/v1/university/semester/get-all-semester");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ApiResponse<List<Semester>> response = new ApiResponse<>(400, e.getMessage(), null,time, "/api/v1/university/semester/get-all-semester");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-semester-by-code")
    public ResponseEntity<ApiResponse<String>> deleteSemesterByCode(@RequestParam String semesterCode, @RequestParam String institutionCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        try {
            String semesterStatus = semesterService.deleteDepartment(semesterCode, institutionCode);
            ApiResponse<String> response = new ApiResponse<>(200, "Semester deleted successfully", semesterStatus, time, "/api/v1/university/semester/delete-semester-by-code");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ApiResponse<String> response = new ApiResponse<>(400, e.getMessage(), null, time, "/api/v1/university/semester/delete-semester-by-code");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
