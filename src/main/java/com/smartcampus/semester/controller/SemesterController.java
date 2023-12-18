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
    public ResponseEntity<ApiResponse<Semester>> addCourseTeacherClass(@RequestParam String semesterCode, @RequestParam String courseCode, @RequestParam String teacherId, @RequestParam String classJoinCode) {
        ApiResponse<Semester> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/semester/add-course-teacher-class");
        try {
            Semester registerSemester = semesterService.addCourseAndTeacherAndClass( semesterCode,  courseCode,  teacherId,  classJoinCode);
            response.setData(registerSemester);
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("SemesterController::addCourseTeacherClass, successfully added Course Teacher and  Class in Semester. Timestamp:{}, ",time);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("SemesterController::addCourseTeacherClass,  fail  to add Course Teacher and  Class  in semester. Timestamp:{},  semesterCode:{}, courseCode:{}, teacherId:{}, classJoinCode:{}, Message:{}",time, semesterCode,courseCode,teacherId,classJoinCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
