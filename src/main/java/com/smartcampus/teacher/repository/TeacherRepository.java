package com.smartcampus.teacher.repository;

import com.smartcampus.teacher.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    @Query("{'email': ?0}")
    Optional<Teacher> findByEmail(String email);

    @Query("{'registrationId': ?0}")
    Optional<Teacher> findByRegistrationId(String teacherRegistrationId);

    @Query("{'teacherId': ?0}")
    Optional<Teacher> findByTeacherId(String teacherId);
}