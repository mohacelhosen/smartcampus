package com.smartcampus.usermanagement.student.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.usermanagement.common.CurrentAddress;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.usermanagement.common.PermanentAddress;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "STUDENT")
public class StudentEntity {
	@Id
	private String id;
	@Indexed(unique = true)
	private String registrationId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime admissionDate;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime graduationDate;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String studentAcademicId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String studentPhotoId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String studentPhotoUrl;

	private String departmentCode;
	private Integer semesterNumber;
	@NonNull
	private String firstName;
	private String lastName;
	private String nickName;
	@Indexed(unique = true)
	private String contactNumber;
	private LocalDate dob;
	private String gender;
	private String nationality;
	private String religion;
	private String birthPlace;
	private String maritalStatus;
	@NotNull
	private String bloodGroup;
	@NotNull
	@Indexed(unique = true)
	private String email;
	private String nidNumber;
	private InternationalStudentInformation internationalStudentInformation;
	private ExtracurricularActivities extracurricularActivities;
	private ParentInfo parentInfo;
	private CurrentAddress presentAddress;
	private PermanentAddress permanentAddress;
	private List<PreviousEducation> previousEducation;
	private LocalGuardian localGuardian;
	private StudyPlan studyPlan;
	private MedicalInformation medicalInformation;
	private ScholarshipsAndFinancialAid scholarshipsAndFinancialAid;
	private ResidenceHallInformation residenceHallInformation;
	private StudentBillingAndFinancialInformation studentBillingAndFinancialInformation;
	private String userPassword;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonIgnore
	private String randomPassword;
	@JsonIgnore
	private String roles;
	@JsonIgnore
	private String verificationBy;
	private String comment;
	private Boolean agreeTermsCondition;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String applicationStatus;
	@JsonIgnore
	private boolean isEnabled = false;
	@JsonIgnore
	private String activateBy;
	@JsonIgnore
	private String deactivateBy;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime createDate = new ModelLocalDateTime(null);
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime lastUpdated;

}
