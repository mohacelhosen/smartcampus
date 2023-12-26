package com.smartcampus.classroom.repoository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.smartcampus.classroom.model.Assignment;

import java.util.List;

public interface AssignmentRepository extends MongoRepository<Assignment, String> {
    List<Assignment> findAllByClassId(String classId);
}
