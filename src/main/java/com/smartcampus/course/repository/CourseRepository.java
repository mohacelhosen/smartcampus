package com.smartcampus.course.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.course.model.Course;

public interface CourseRepository extends MongoRepository<Course, String> {
    @Query("{'courseCode': ?0}")
    Optional<Course> findByCourseCode(String courseCode);
}
