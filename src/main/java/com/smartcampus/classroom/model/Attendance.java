package com.smartcampus.classroom.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Attendance {
	@Id
	private String id;
	private String institutionCode;
	private String classId;
	private String classTitle;
	private Date attendanceDate;
	private List<StudentAttendance> presentStudentIds; // 193071002, true
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime attendanceCreationTime;
}
