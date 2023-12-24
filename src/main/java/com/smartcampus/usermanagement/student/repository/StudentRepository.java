package com.smartcampus.usermanagement.student.repository;

import com.smartcampus.usermanagement.student.model.StudentEntity;
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

    @Query("{'institutionCode': ?0, 'departmentCode': ?1, 'semesterNumber': ?2}")
    List<StudentEntity> findByInstitutionCodeAndDepartmentCodeAndSemesterNumber(String institutionCode, String departmentCode, Integer semesterNumber);

}

