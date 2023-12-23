package com.smartcampus.usermanagement.teacher.repository;

import com.smartcampus.usermanagement.teacher.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    @Query("{'email': ?0}")
    Optional<Teacher> findByEmail(String email);

    List<Teacher> findAllByInstitutionCode(String institutionCode);
    @Query("{'registrationId': ?0}")
    Optional<Teacher> findByRegistrationId(String teacherRegistrationId);

    @Query("{'teacherAcademicId': ?0}")
    Optional<Teacher> findByTeacherId(String teacherAcademicId);
}
