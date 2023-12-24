package com.smartcampus.department.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcampus.course.model.Course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Department {
	@Id
	private String id;
	private String institutionCode;
	private String departmentGroupName;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String departmentGroupCode;
	private String departmentName;
	private String departmentShortName;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String departmentCode;
	private Integer departmentCodeInNumber;
	private Integer totalSemester;
	List<Course> courseList;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime createdDateTime;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime updateDateTime;
	private String createdBy;
}
