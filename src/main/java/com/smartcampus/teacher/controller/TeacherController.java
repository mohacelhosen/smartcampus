package com.smartcampus.teacher.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.teacher.model.Teacher;
import com.smartcampus.teacher.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Teacher>> register(@RequestBody Teacher teacher){
        ApiResponse<Teacher> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/register");
        try{
            Teacher registerTeacher = teacherService.registerTeacher(teacher);
            response.setData(registerTeacher);
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("TeacherController::register, teacher register successfully. Timestamp:{},  Registration Id:{}",time, registerTeacher.getRegistrationId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (AlreadyExistsException e){
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("TeacherController::register, teacher register fail. Timestamp:{},  Input:{}, Message:{}",time, teacher, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Teacher>> updateTeacherInfo(@RequestBody Teacher teacher){
        ApiResponse<Teacher> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/update");
        try{
            Teacher registerTeacher = teacherService.updateTeacher(teacher);
            response.setData(registerTeacher);
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("TeacherController::update, teacher information update successfully. Timestamp:{},  Registration Id:{}, Teacher ID:{}",time, registerTeacher.getRegistrationId(), registerTeacher.getTeacherId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (AlreadyExistsException e){
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("TeacherController::update, fail to update teacher information. Timestamp:{},  Input:{}, Message:{}",time, teacher, e.getMessage(),e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
