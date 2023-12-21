package com.smartcampus.role.repository;


import com.smartcampus.role.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
}
