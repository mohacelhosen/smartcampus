package com.smartcampus.classroom.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Submission {
	private String studentId; // The student who submitted the assignment
	private String submissionText; // Text submitted by the student
	private List<String> attachmentDocumentIds; // id of attachments submitted by the student
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime submissionDate; // Date and time of submission
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private int grade; // Grade given by the teacher
}
