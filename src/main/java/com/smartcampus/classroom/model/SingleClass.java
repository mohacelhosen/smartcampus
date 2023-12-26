package com.smartcampus.classroom.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.library.model.Books;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class SingleClass {
	@Id
	private String id;
	private String teacherRegistrationId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String classJoinCode;
	private String classTitle;// courseTitle
	private String courseCode;
	private String courseCredit;
	private String classStatus; // online or offline
	private String classRoomNumber; // physical class room
	private String teacherName;
	private String teacherEmail;
	private String institutionCode;
	private List<String> teacherContact;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer totalStudent;
	private List<Notes> teacherLectureSheets;
	private List<ContactInfo> crContact;// name & contactNumber
	private List<Books> referenceBooks;
	private List<Objective> classObjectives;
	private String classLink;
	private Set<StudentInfoForClassRoom> students;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<String> assignmentList;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<String> examsList;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<String> attendancesLIst;

	private ModelLocalDateTime classCreationDateTime = new ModelLocalDateTime(null);
	private String classCreatedBy;
}
