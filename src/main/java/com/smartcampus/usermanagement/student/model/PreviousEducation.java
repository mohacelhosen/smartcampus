package com.smartcampus.usermanagement.student.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreviousEducation {
	private String institutionName; // Name of the previous educational institution
	private String degreeEarned; // Academic degree or qualification obtained
	private String fieldOfStudy; // Field of study or major
	private String graduationYear; // Year of graduation or completion
	private String academicAchievements; // Notable academic achievements or honors
	private String transcriptName;
//    private FileDto transcript;      // Information about transcripts or academic records from the previous institution
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String transcriptsId;
}
