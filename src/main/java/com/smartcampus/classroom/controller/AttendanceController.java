package com.smartcampus.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.classroom.model.Attendance;
import com.smartcampus.classroom.model.StudentAttendance;
import com.smartcampus.classroom.service.AttendanceService;

@RestController
@RequestMapping("/api/v1/university/classroom/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	@PostMapping("/save")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public Attendance saveAttendance(@RequestBody Attendance attendance) {
		return attendanceService.saveToDb(attendance);
	}

	@PutMapping("/update")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public Attendance updateAttendance(@RequestBody Attendance attendance) {
		return attendanceService.updateAttendance(attendance);
	}

	@PutMapping("/update/{attendanceRecordId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public Attendance updateAttendance(@PathVariable String attendanceRecordId,
			@RequestBody List<StudentAttendance> newPresentStatusList) {
		return attendanceService.updateAttendance(attendanceRecordId, newPresentStatusList);
	}

	@GetMapping("/total-present-days/{classId}/{studentId}")
//	@PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_DEVELOPER')")
	public Integer getTotalPresentDaysForStudent(@PathVariable String classId, @PathVariable String studentId) {
		return attendanceService.getTotalPresentDaysForStudent(classId, studentId);
	}
}
