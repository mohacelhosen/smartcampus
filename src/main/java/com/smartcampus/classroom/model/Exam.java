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
public class Exam {
	@Id
	private String id;
	private String classId;
	private String classTitle;
	private String classDescription;
	private List<String> examDocumentIds;
	private Date examDate;
	private Boolean submissionEnable = true;
	private List<Submission> examSubmissions; // List of student exam submissions
	private boolean isGraded; // Indicates if the exam has been graded
	private String markForThisExam;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime examCreationTime;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime examUpdateTime;
}
