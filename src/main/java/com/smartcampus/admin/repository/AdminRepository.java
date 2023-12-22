package com.smartcampus.admin.repository;

import com.smartcampus.security.model.CustomUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartcampus.admin.model.Admin;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    @Query("{'academicId': ?0, 'institutionCode':?1}")
    Optional<Admin> findByAcademicIdAndInstitutionCode(String academicId, String institutionCode);
    @Query("{'institutionCode':?0}")
    List<Admin> findAllByInstitutionCode(String institutionCode);
}
