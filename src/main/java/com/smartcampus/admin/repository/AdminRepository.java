package com.smartcampus.admin.repository;

import com.smartcampus.security.model.CustomUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartcampus.admin.model.Admin;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByUserId(String userId);
}
