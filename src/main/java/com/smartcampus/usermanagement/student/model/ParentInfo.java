package com.smartcampus.usermanagement.student.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ParentInfo {
	private String fatherName;
//    private FileDto fatherPhoto;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String fatherPhotoId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String fatherPhotoUrl;
	private String fatherOccupation;
	private String fatherContactNumber;
	private String motherName;
//    private FileDto motherPhoto;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String motherPhotoId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String motherPhotoUrl;
	private String motherOccupation;
	private String motherContactNumber;
	private Integer totalFamilyMember;
	private Integer totalBrotherIncludingYou;
	private Integer totalSisterIncludingYou;
	private Boolean isAnySiblingStudyHere;

}
