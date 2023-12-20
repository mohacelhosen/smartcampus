package com.smartcampus.semester.repository;

import com.smartcampus.department.model.Department;
import com.smartcampus.semester.model.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends MongoRepository<Semester, String> {

    @Query("{'semesterCode': ?0}")
    Optional<Semester> findBySemesterCode(String semesterCode);
    @Query("{'departmentCode': ?0}")
    List<Semester> findByDepartmentCode(String departmentCode);

    @Query("{'departmentCode': ?0, 'semesterNumber': ?1}")
    Optional<Semester> findByDepartmentCodeAndSemesterNumber(String departmentCode, Integer semesterNumber);
}
