package com.smartcampus.classroom.repoository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.classroom.model.SingleClass;

public interface SingleClassRepository extends MongoRepository<SingleClass, String> {
	Optional<SingleClass> findByClassJoinCode(String classJoinCode);

	@Query("{'students.academicId': ?0}")
	List<SingleClass> findByStudentsAcademicId(String studentAcademicId);

	@Query("{'teacherId': ?0}")
	List<SingleClass> findByTeacherId(String teacherId);

	@Query(value = "{'id': ?0, 'students.academicId': {$exists: true}}", fields = "{'students.academicId': 1, 'students.studentName': 1}")
	List<SingleClass> findAllStudentIdsAndNames(String classId);

}
