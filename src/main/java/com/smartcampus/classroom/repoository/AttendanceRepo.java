package com.smartcampus.classroom.repoository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.smartcampus.classroom.model.Attendance;

public interface AttendanceRepo extends MongoRepository<Attendance, String> {

	@Query("{'classId': ?0, 'presentStudentIds.studentId': ?1}")
	List<Attendance> findByPresentStudentIds(String classId, String studentId);

}
