package com.smartcampus.Student.repository;

import com.smartcampus.Student.model.StudentEntity;
import com.smartcampus.teacher.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<StudentEntity, String> {
    @Query("{'registrationId': ?0}")
    Optional<StudentEntity> findByRegistrationId(String registrationId);
    @Query("{'studentAcademicId': ?0}")
    Optional<StudentEntity> findByStudentAcademicId(String studentAcademicId);
    Optional<StudentEntity> findByEmail(String studentEmail);

    @Query("{'departmentCode':?0, 'semesterNumber': ?1 }")
    List<StudentEntity> findByDepartmentCodeAndSemesterNumber(String departmentCode, Integer semesterNumber);

}

