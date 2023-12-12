package com.smartcampus.classroom.repoository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartcampus.classroom.model.Assignment;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {

}
