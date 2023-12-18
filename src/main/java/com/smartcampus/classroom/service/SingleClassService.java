package com.smartcampus.classroom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcampus.classroom.model.JoinInClass;
import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.model.StudentInfoForClassRoom;
import com.smartcampus.classroom.repoository.SingleClassRepository;

@Service
public class SingleClassService {

	@Autowired
	private SingleClassRepository singleClassRepository;

	public SingleClass save(SingleClass singleClass) {
		String classCode = String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-", "");
		singleClass.setClassJoinCode(classCode);
		if (singleClass.getStudents() != null && !singleClass.getStudents().isEmpty()) {
			singleClass.setTotalStudent(singleClass.getStudents().size());
		} else {
			singleClass.setTotalStudent(0);
		}
		SingleClass dbClass = singleClassRepository.save(singleClass);
		dbClass.setTotalStudent(dbClass.getStudents().size());
		return dbClass;
	}

	public SingleClass update(String classId, SingleClass updatedClass) {
		Optional<SingleClass> optionalDbClass = singleClassRepository.findById(classId);

		if (optionalDbClass.isPresent()) {
			SingleClass dbClass = optionalDbClass.get();

			// Update only the necessary fields
			if (updatedClass.getTeacherId() != null && !updatedClass.getTeacherId().isEmpty()) {
				dbClass.setTeacherId(updatedClass.getTeacherId());
			}

			if (updatedClass.getClassTitle() != null && !updatedClass.getClassTitle().isEmpty()) {
				dbClass.setClassTitle(updatedClass.getClassTitle());
			}

			if (updatedClass.getCourseCode() != null && !updatedClass.getCourseCode().isEmpty()) {
				dbClass.setCourseCode(updatedClass.getCourseCode());
			}

			if (updatedClass.getCourseCredit() != null && !updatedClass.getCourseCredit().isEmpty()) {
				dbClass.setCourseCredit(updatedClass.getCourseCredit());
			}

			if (updatedClass.getClassStatus() != null && !updatedClass.getClassStatus().isEmpty()) {
				dbClass.setClassStatus(updatedClass.getClassStatus());
			}

			if (updatedClass.getClassRoomNumber() != null && !updatedClass.getClassRoomNumber().isEmpty()) {
				dbClass.setClassRoomNumber(updatedClass.getClassRoomNumber());
			}

			if (updatedClass.getTeacherName() != null && !updatedClass.getTeacherName().isEmpty()) {
				dbClass.setTeacherName(updatedClass.getTeacherName());
			}

			if (updatedClass.getTeacherEmail() != null && !updatedClass.getTeacherEmail().isEmpty()) {
				dbClass.setTeacherEmail(updatedClass.getTeacherEmail());
			}

			if (updatedClass.getTeacherContact() != null && !updatedClass.getTeacherContact().isEmpty()) {
				dbClass.setTeacherContact(updatedClass.getTeacherContact());
			}

			if (updatedClass.getTeacherLectureSheets() != null && !updatedClass.getTeacherLectureSheets().isEmpty()) {
				dbClass.setTeacherLectureSheets(updatedClass.getTeacherLectureSheets());
			}

			if (updatedClass.getCrContact() != null && !updatedClass.getCrContact().isEmpty()) {
				dbClass.setCrContact(updatedClass.getCrContact());
			}

			if (updatedClass.getReferenceBooks() != null && !updatedClass.getReferenceBooks().isEmpty()) {
				dbClass.setReferenceBooks(updatedClass.getReferenceBooks());
			}

			if (updatedClass.getClassObjectives() != null && !updatedClass.getClassObjectives().isEmpty()) {
				dbClass.setClassObjectives(updatedClass.getClassObjectives());
			}

			if (updatedClass.getClassLink() != null && !updatedClass.getClassLink().isEmpty()) {
				dbClass.setClassLink(updatedClass.getClassLink());
			}

			if (updatedClass.getStudents() != null && !updatedClass.getStudents().isEmpty()) {
				dbClass.setStudents(updatedClass.getStudents());
				dbClass.setTotalStudent(dbClass.getStudents().size());
			}

			// Save the updated class
			return singleClassRepository.save(dbClass);
		} else {
			throw new RuntimeException("Class not found for ID: " + classId);
		}
	}

	public SingleClass findSingleClass(String classId) {
		return singleClassRepository.findById(classId)
				.orElseThrow(() -> new RuntimeException("Class not found for ID: " + classId));
	}

	public Boolean delete(String classId) {
		Optional<SingleClass> optionalClass = singleClassRepository.findById(classId);
		if (optionalClass.isPresent()) {
			singleClassRepository.deleteById(classId);
			return true;
		} else {
			throw new RuntimeException("Class not found for ID: " + classId);
		}
	}

	public String joinInClass(JoinInClass joinForClass) {
		Optional<SingleClass> optionalDbClass = singleClassRepository
				.findByClassJoinCode(joinForClass.getClassJoinCode());

		if (optionalDbClass.isPresent()) {
			SingleClass dbClass = optionalDbClass.get();

			// Check if the student with the given academicId is already in the class
			boolean isStudentAlreadyJoined = dbClass.getStudents().stream()
					.anyMatch(student -> student.getAcademicId().equals(joinForClass.getAcademicId()));

			if (!isStudentAlreadyJoined) {
				Set<StudentInfoForClassRoom> students = dbClass.getStudents();
				StudentInfoForClassRoom student = new StudentInfoForClassRoom();
				student.setAcademicId(joinForClass.getAcademicId());
				student.setStudentName(joinForClass.getStudentName());
				students.add(student);
				dbClass.setStudents(students);
				singleClassRepository.save(dbClass);
				return "Join Successful";
			} else {
				return "Student with academicId " + joinForClass.getAcademicId() + " is already part of the class.";
			}
		}
		return "Invalid joining code!";
	}

	public List<SingleClass> getAllClassByStudentId(String academicId) {
		return singleClassRepository.findByStudentsAcademicId(academicId);
	}

	public List<SingleClass> getAllClassByTeacherId(String teacherId) {
		return singleClassRepository.findByTeacherId(teacherId);
	}

	public List<SingleClass> getAllClasses() {
		return singleClassRepository.findAll();
	}

	public List<StudentInfoForClassRoom> singleClassStudents(String classId) {
		List<SingleClass> classes = singleClassRepository.findAllStudentIdsAndNames(classId);

		List<StudentInfoForClassRoom> studentInfoList = new ArrayList<>();

		for (SingleClass singleClass : classes) {
			Set<StudentInfoForClassRoom> students = singleClass.getStudents();
			for (StudentInfoForClassRoom student : students) {
				studentInfoList.add(student);
			}
		}
		return studentInfoList;
	}

}
