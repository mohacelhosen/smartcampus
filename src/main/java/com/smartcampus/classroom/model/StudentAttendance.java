package com.smartcampus.classroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendance {
	private String institutionCode;
	private String studentId;
	private String studentName;
	private boolean isPresent;
}
