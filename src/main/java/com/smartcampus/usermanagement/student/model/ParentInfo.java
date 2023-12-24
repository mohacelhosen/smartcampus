package com.smartcampus.usermanagement.student.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentInfo {
	private String fatherPhotoId;
	private String fatherOccupation;
	private String fatherContactNumber;
	private String motherPhotoId;
	private String motherOccupation;
	private String motherContactNumber;
	private Integer totalFamilyMember;
	private Integer totalBrotherIncludingYou;
	private Integer totalSisterIncludingYou;
	private Boolean isAnySiblingStudyHere;

}
