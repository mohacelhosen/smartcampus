package com.smartcampus.course.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;
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
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime createdDateTime;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime updateDateTime;
	private String createdBy;
}
