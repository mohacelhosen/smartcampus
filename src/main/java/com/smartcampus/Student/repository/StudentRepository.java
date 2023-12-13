package com.smartcampus.Student.repository;

import com.smartcampus.Student.model.StudentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<StudentEntity, String> {
}
