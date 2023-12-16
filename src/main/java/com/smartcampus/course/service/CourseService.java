package com.smartcampus.course.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartcampus.course.model.Course;
import com.smartcampus.course.repository.CourseRepository;
import com.smartcampus.exception.IncompleteDataException;
import com.smartcampus.exception.NotFoundException;

@Service
public class CourseService {
	@Autowired
	private CourseRepository courseRepository;

	@Transactional
	public Course register(Course course) {
		if (valid(course)) {
			return courseRepository.save(course);
		} else {
			throw new IncompleteDataException("Incomplete course or duplicate courseCode not allowed to register");
		}
	}

	public Course findByCourseId(String courseId) {
		return courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Invalid course id"));
	}

	public Course findByCourseCode(String courseCode) {
		return courseRepository.findByCourseCode(courseCode)
				.orElseThrow(() -> new NotFoundException("Invalid course courseCode"));
	}

	public List<Course> getAllCourse() {
		return courseRepository.findAll();
	}

	private String deleteCourseByCourseCode(String courseCode) {
		Optional<Course> findByCourseCode = courseRepository.findByCourseCode(courseCode);
		Course dbCourse = findByCourseCode.get();
		if (dbCourse != null) {
			courseRepository.delete(dbCourse);
			return "Delete Successfully";
		} else {
			throw new NotFoundException("Invalid course courseCode");
		}

	}

	public boolean valid(Course course) {
		return course != null && course.getCourseTitle() != null && !course.getCourseTitle().isEmpty()
				&& course.getCourseCredits() != null && !course.getCourseCredits().isEmpty()
				&& course.getCourseCode() != null && !course.getCourseCode().isEmpty()
				&& !courseCodeExists(course.getCourseCode());
	}

	private boolean courseCodeExists(String courseCode) {
		return courseRepository.findByCourseCode(courseCode).isPresent();
	}
}
