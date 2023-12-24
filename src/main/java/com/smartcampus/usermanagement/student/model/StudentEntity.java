package com.smartcampus.usermanagement.student.model;

import java.time.LocalDate;
import java.util.List;

import com.smartcampus.usermanagement.common.User;
import com.smartcampus.usermanagement.common.CurrentAddress;
import com.smartcampus.usermanagement.common.PermanentAddress;
import com.smartcampus.common.ModelLocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "STUDENT")
public class StudentEntity extends User {
	@Id
	private String id;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime admissionDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime graduationDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String studentAcademicId;
	private String studentPhotoId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String studentPhotoUrl;

	private String departmentCode;
	private Integer semesterNumber;
	private String nidNumber;
	private InternationalStudentInformation internationalStudentInformation;
	private ExtracurricularActivities extracurricularActivities;
	private ParentInfo parentInfo;
	private List<PreviousEducation> previousEducation;
	private LocalGuardian localGuardian;
	private StudyPlan studyPlan;
	private MedicalInformation medicalInformation;
	private ScholarshipsAndFinancialAid scholarshipsAndFinancialAid;
	private ResidenceHallInformation residenceHallInformation;
	private StudentBillingAndFinancialInformation studentBillingAndFinancialInformation;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonIgnore
	private String randomPassword;

	private String comment;
	private Boolean agreeTermsCondition;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String applicationStatus;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime createDate = new ModelLocalDateTime(null);

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ModelLocalDateTime lastUpdated;
}
