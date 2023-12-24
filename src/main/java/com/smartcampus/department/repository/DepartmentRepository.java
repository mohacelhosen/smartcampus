package com.smartcampus.department.repository;

import com.smartcampus.department.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, String> {
    @Query("{'departmentCode':?0, 'institutionCode': ?1}")
    Optional<Department> findByDepartmentCodeAndInstitutionCode(String departmentCode,String institutionCode);

    @Query("{'institutionCode': ?0, 'departmentCodeInNumber':?1 }")
    Optional<Department> findByInstitutionCodeAndDepartmentCodeInNumber(String institutionCode, Integer departmentCodeInNumber);
}
