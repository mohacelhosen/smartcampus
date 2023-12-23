package com.smartcampus.classroom.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoForClassRoom {
	private String academicId;
	private String studentName;
	private String institutionCode;
	private List<Notes> studentNote;
}
