package com.smartcampus.admin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartcampus.admin.model.Admin;

public interface AdminRepository extends MongoRepository<Admin, String> {

}
