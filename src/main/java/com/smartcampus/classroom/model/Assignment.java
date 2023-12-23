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
public class Assignment {
	@Id
	private String id;
	private String institutionCode;
	private String classId;
	private String classTitle;
	private String classDescription;
	private List<String> assignmentDocumentIds;
	private Date dueDate;
	private List<Submission> submissions; // List of student submissions
	private boolean isGraded; // Indicates if the assignment has been graded
	private String markForThisAssignment;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime assignmentCreationTime;
}
