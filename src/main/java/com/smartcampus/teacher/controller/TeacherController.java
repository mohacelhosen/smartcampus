package com.smartcampus.teacher.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.exception.AlreadyExistsException;
import com.smartcampus.exception.NotFoundException;
import com.smartcampus.teacher.model.Teacher;
import com.smartcampus.teacher.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Teacher>> register(@RequestBody Teacher teacher) {
        ApiResponse<Teacher> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/register");
        try {
            Teacher registerTeacher = teacherService.registerTeacher(teacher);
            response.setData(registerTeacher);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Teacher register successfully");
            logger.info("TeacherController::register, teacher register successfully. Timestamp:{},  Registration Id:{}", time, registerTeacher.getRegistrationId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("TeacherController::register, teacher register fail. Timestamp:{},  Input:{}, Message:{}", time, teacher, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Teacher>> updateTeacherInfo(@RequestBody Teacher teacher) {
        ApiResponse<Teacher> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/update");
        try {
            Teacher registerTeacher = teacherService.updateTeacher(teacher);
            response.setData(registerTeacher);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Update Successfully");
            logger.info("TeacherController::update, teacher information update successfully. Timestamp:{},  Registration Id:{}, Teacher ID:{}", time, registerTeacher.getRegistrationId(), registerTeacher.getTeacherId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            logger.info("TeacherController::update, fail to update teacher information. Timestamp:{},  Input:{}, Message:{}", time, teacher, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find-by-registration-id")
    public ResponseEntity<ApiResponse<Teacher>> getTeacherByRegistrationId(@RequestParam String registrationId) {
        ApiResponse<Teacher> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/find-by-registration-id");
        try {
            Teacher teacher = teacherService.findTeacherByRegistration(registrationId);
            response.setData(teacher);
            response.setMessage("Retrieve Successfully");
            response.setStatus(HttpStatus.OK.value());
            logger.info("TeacherController::getTeacherByRegistrationId, teacher info retriv successfully. Timestamp:{},  Registration Id:{}", time, registrationId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            logger.info("TeacherController::getTeacherByRegistrationId, fail to retriv teacher information using registration id. Timestamp:{},  Input:{}, Message:{}", time, registrationId, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-by-teacher-academic-id")
    public ResponseEntity<ApiResponse<Teacher>> getTeacherByAcademicId(@RequestParam String teacherId) {
        ApiResponse<Teacher> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/find-by-teacher-academic-id");
        try {
            Teacher teacher = teacherService.findTeacherByTeacherId(teacherId);
            response.setData(teacher);
            response.setMessage("Retrieve Successfully");
            response.setStatus(HttpStatus.OK.value());
            logger.info("TeacherController::getTeacherByAcademicId, teacher info retriv successfully. Timestamp:{},  Registration Id:{}", time, teacherId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            logger.info("TeacherController::getTeacherByAcademicId, fail to retriv teacher information using registration id. Timestamp:{},  Input:{}, Message:{}", time, teacherId, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all-teacher")
    public ResponseEntity<ApiResponse<List<Teacher>>> getAllTeacher() {
        ApiResponse<List<Teacher>> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/teacher/all-teacher");

        List<Teacher> teacher = teacherService.findAllTeacher();
        response.setData(teacher);
        response.setMessage("Retrieve Successfully");
        response.setStatus(HttpStatus.OK.value());
        logger.info("TeacherController::getAllTeacher, teacher info retriv successfully. Timestamp:{}", time);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
