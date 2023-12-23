package com.smartcampus.classroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinInClass {
	private String institutionCode;
	private String academicId;
	private String studentName;
	private String classJoinCode;
}
