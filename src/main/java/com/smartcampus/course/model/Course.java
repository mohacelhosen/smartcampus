package com.smartcampus.course.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Course {
	@Id
	private String id;
	private String courseTitle;
	private String courseCode;
	private String courseCredits;
	private Boolean courseOptional;
}
