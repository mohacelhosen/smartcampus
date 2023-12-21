package com.smartcampus.usermanagement.student.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalGuardian {
	private String localGuardianName;
//    private FileDto localGuardianPhoto;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String localGuardianPhotoId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String localGuardianPhotoUrl;
	private String relationship;
	private String guardianOccupation;
	private String emailOfLocalGuardian;
	private String photoOfLocalGuardian;
	private String district;
	private String policeStation;
	private Integer postalCode;
	private String address;

}
