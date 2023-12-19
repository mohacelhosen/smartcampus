package com.smartcampus.Student.controller;

import com.smartcampus.Student.model.StudentEntity;
import com.smartcampus.Student.service.StudentService;
import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university/student")
@CrossOrigin("*")
public class StudentController {
    @Autowired
     private StudentService studentService;
    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<StudentEntity>> register(@RequestBody StudentEntity studentEntity){
        ApiResponse<StudentEntity> response = new ApiResponse<>();
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        String requestid = RequestId.generateRequestId();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/student/registration");

        try {
            StudentEntity registerStudent = studentService.registerStudent(studentEntity);
            response.setData(registerStudent);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Student Register Successfully !");

            return new ResponseEntity<>(response,HttpStatus.OK);

        }
        catch (RuntimeException e ){
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }
}
