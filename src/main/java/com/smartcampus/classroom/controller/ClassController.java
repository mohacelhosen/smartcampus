package com.smartcampus.classroom.controller;

import java.util.List;

import com.smartcampus.classroom.model.ClassAnnouncement;
import com.smartcampus.classroom.service.ClassAnnouncementService;
import com.smartcampus.common.ApiResponse;
import com.smartcampus.common.ModelLocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.classroom.model.JoinInClass;
import com.smartcampus.classroom.model.SingleClass;
import com.smartcampus.classroom.model.StudentInfoForClassRoom;
import com.smartcampus.classroom.service.SingleClassService;

@RestController
@RequestMapping("/api/v1/university/classroom")
@CrossOrigin("*")
public class ClassController {
	@Autowired
	private SingleClassService singleClassService;

	@Autowired
	private ClassAnnouncementService classAnnouncementService;
	@PostMapping("/register")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TEACHER', 'ROLE_DEVELOPER')")
	public ResponseEntity<SingleClass> registerSingleClass(@RequestBody SingleClass sinlgeClass) {
		SingleClass save = singleClassService.save(sinlgeClass);
		return new ResponseEntity<>(save, HttpStatus.OK);
	}

	@PostMapping("/delete-by-id")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEVELOPER')")
	public ResponseEntity<?> deleteClassById(@RequestParam String classId) {
		Boolean delete = singleClassService.delete(classId);
		return new ResponseEntity<>(delete, HttpStatus.OK);
	}

	@PostMapping("/update")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TEACHER', 'ROLE_DEVELOPER')")
	public ResponseEntity<SingleClass> updateSingleClassInfo(@RequestBody SingleClass updatedClass) {
		SingleClass save = singleClassService.update(updatedClass.getId(), updatedClass);
		return new ResponseEntity<>(save, HttpStatus.OK);
	}

	@PostMapping("/class-info-by-id")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BOARD_MEMBER', 'ROLE_DEVELOPER')")
	public ResponseEntity<SingleClass> getSingleClassInfo(@RequestParam String sinlgeClassId) {
		SingleClass save = singleClassService.findSingleClass(sinlgeClassId);
		return new ResponseEntity<>(save, HttpStatus.OK);
	}

	@PostMapping("/join")
//	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_ADMIN', 'ROLE_DEVELOPER')")
	public ResponseEntity<String> joinInClass(@RequestBody JoinInClass joinInClass) {
		String response = singleClassService.joinInClass(joinInClass);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/get-all-class")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEVELOPER')")
	public ResponseEntity<List<SingleClass>> getAllClasses() {
		List<SingleClass> allClasses = singleClassService.getAllClasses();
		return new ResponseEntity<>(allClasses, HttpStatus.OK);
	}

	@PostMapping("/myclasses-student")
//	@PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_DEVELOPER')")
	public ResponseEntity<List<SingleClass>> getAllClassByStudentId(@RequestParam String academicId) {
		List<SingleClass> response = singleClassService.getAllClassByStudentId(academicId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/myclasses-teacher")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public ResponseEntity<List<SingleClass>> getAllTeacherId(@RequestParam String academicId) {
		List<SingleClass> response = singleClassService.getAllClassByTeacherId(academicId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/total-students/{classId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_STUDENT', 'ROLE_ADMIN','ROLE_DEVELOPER')")
	public ResponseEntity<List<StudentInfoForClassRoom>> singleClassStudents(@RequestParam String classId) {
		List<StudentInfoForClassRoom> response = singleClassService.singleClassStudents(classId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create-class-announcement")
	public ResponseEntity<ApiResponse<ClassAnnouncement>> createAnnouncement(@RequestBody ClassAnnouncement classAnnouncement) {
		String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
		ClassAnnouncement createdAnnouncement = classAnnouncementService.createAnnouncement(
				classAnnouncement.getTeacherAcademicId(),
				classAnnouncement.getDepartmentCode(),
				classAnnouncement.getSemesterNumber(),
				classAnnouncement.getCourseCode(),
				classAnnouncement.getClassStartSession()
		);
		ApiResponse<ClassAnnouncement> response = new ApiResponse<>(200, "Announcement created successfully", createdAnnouncement,time, "/api/v1/university/classroom/create-class-announcement");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/get-class-announcement")
	public ResponseEntity<ApiResponse<List<ClassAnnouncement>>> getAllAnnouncement(@RequestParam String departmentCode, @RequestParam Integer semesterNumber) {
		String time = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
		List<ClassAnnouncement> announcements = classAnnouncementService.findAllByDepartmentCodeAndSemesterNumber(departmentCode, semesterNumber);
		ApiResponse<List<ClassAnnouncement>> response = new ApiResponse<>(200, "Announcements retrieved successfully", announcements,time, "/api/v1/university/classroom/get-class-announcement");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
