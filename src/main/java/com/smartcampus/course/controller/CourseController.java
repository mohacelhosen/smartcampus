package com.smartcampus.course.controller;

import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.RequestId;
import com.smartcampus.course.model.Course;
import com.smartcampus.course.service.CourseService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/university/course")
@CrossOrigin("*")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    @Autowired
    private CourseService courseService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Course>> registerCourse(@RequestBody Course course) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Course> response;

        try {
            Course registeredCourse = courseService.register(course);
            response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Course registered successfully",
                    registeredCourse,
                    time,
                    "/api/v1/university/course/register"
            );

            // Log successful registration
            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("CourseController::registerCourse, Successfully registered course. Course ID: {}, Title: {}, Timestamp: {}", registeredCourse.getId(), registeredCourse.getCourseTitle(), time);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IncompleteDataException e) {
            // Log incomplete data exception
            logger.error("CourseController::registerCourse, Error registering course due to incomplete or duplicate data. Input Course: {}, Message: {}", course, e.getMessage(), e);

            response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Incomplete or duplicate data",
                    null,
                    time,
                    "/api/v1/university/course/register"
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            // Log unexpected exceptions
            logger.error("CourseController::registerCourse, Unexpected error during course registration. Message: {}", ex.getMessage(), ex);

            response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    null,
                    time,
                    "/api/v1/university/course/register"
            );

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Course>> updateCourseInfo(@RequestBody Course course) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Course> response = new ApiResponse<>();
        response.setEndpoint("/api/v1/university/course/update");
        response.setTimestamp(time);
        try {
            Course courseInfo = courseService.updateCourseInfo(course.getId(), course);
            response.setMessage("Update Successfully");
            response.setStatus(HttpStatus.OK.value());
            response.setData(courseInfo);

            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("CourseController::updateCourseInfo, Successfully update course information. Course ID: {}, Title: {}, Timestamp: {}", course.getId(), course.getCourseTitle(), time);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("CourseController::updateCourseInfo, Error updating the course information due to Invalid course Id. Input Course: {}, Message: {}", course, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-by-course-id")
    public ResponseEntity<ApiResponse<Course>> getSingleCourseInfo(@RequestParam String courseId) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Course> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/course/get-by-course-id");

        try {
            Course courseInfo = courseService.findByCourseId(courseId);
            response.setMessage("Successfully retriv course information");
            response.setStatus(HttpStatus.OK.value());
            response.setData(courseInfo);

            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("CourseController::getSingleCourseInfo, Successfully retriv course information. Course ID: {}, response: {}", courseId, courseInfo);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("CourseController::getSingleCourseInfo, Invalid course Id. Input Course: {}, Message: {}", courseId, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-by-course-code")
    public ResponseEntity<ApiResponse<Course>> getSingleCourseInfoByCode(@RequestParam String courseCode, @RequestParam String institutionCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<Course> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/course/get-by-course-code");

        try {
            Course courseInfo = courseService.findByCourseCode(courseCode, institutionCode);
            response.setMessage("Successfully retriv course information");
            response.setStatus(HttpStatus.OK.value());
            response.setData(courseInfo);

            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("CourseController::getSingleCourseInfoByCode, Successfully retriv course information. Course ID: {}, response: {}", courseCode, courseInfo);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("CourseController::getSingleCourseInfoByCode, Invalid course Id. Input Course: {}, Message: {}", courseCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourseInfo() {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<List<Course>> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/course/get-all");

        List<Course> courseInfo = courseService.findAll();
        response.setMessage("Successfully retriv course information");
        response.setStatus(HttpStatus.OK.value());
        response.setData(courseInfo);
        MDC.put("requestId", RequestId.generateRequestId());
        logger.info("CourseController::getAllCourseInfo,  Successfully retriv All course information. total course: {}", courseInfo.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-course-code")
    public ResponseEntity<ApiResponse<String>> deleteCourseByCourseCode(@RequestParam String courseCode, @RequestParam String institutionCode) {
        String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
        ApiResponse<String> response = new ApiResponse<>();
        response.setTimestamp(time);
        response.setEndpoint("/api/v1/university/course/delete-by-course-code");

        try {
            String message = courseService.deleteCourseByCourseCode(courseCode,institutionCode);
            response.setData(message);
            response.setStatus(HttpStatus.OK.value());
            MDC.put("requestId", RequestId.generateRequestId());
            logger.info("CourseController::deleteCourseByCourseCode,  Successfully delete course. course code: {}", courseCode);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {

            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            logger.error("CourseController::deleteCourseByCourseCode, Invalid course Id. Input Course: {}, Message: {}", courseCode, e.getMessage(), e);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}