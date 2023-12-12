package com.smartcampus.classroom.repoository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.classroom.model.Exam;

public interface ExamRepo extends MongoRepository<Exam, String> {

	@Query(value = "{'id': ?0, 'examSubmissions.studentId': {$exists: true}}", fields = "{'stuexamSubmissionsdents.studentId': 1}")
	List<Exam> findAllStudentIds(String examId);
}
