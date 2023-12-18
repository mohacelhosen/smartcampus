package com.smartcampus.teacher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.CurrentAddress;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.PermanentAddress;
import com.smartcampus.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "Teacher")
public class Teacher {
    @Id
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String registrationId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String teacherId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String roles;
    private String designation;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String sectionName; // admission
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String sectionCode; // admission-401
    private String userPhotoId;
    private String photoUrl;
    private String firstName;
    private String lastName;
    private String nickName;
    private String fatherName;
    private String fatherOccupation;
    private String motherName;
    private String motherOccupation;
    private String contactNumber;
    private LocalDate dob;
    private String gender;
    private String nationality;
    private String religion;
    private String birthPlace;
    private String maritalStatus;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approveStatus;
    private String bloodGroup;
    private String email;
    private String nidNumber;
    private CurrentAddress currentAddress;
    private PermanentAddress permanentAddress;

    // Educational Qualifications
    private List<EducationalQualification> educationalQualifications;

    // Certifications and Professional Development
    private List<Certification> certifications;

    // Teaching Subjects
    private List<Course> teachingSubjects;

    // Languages Spoken
    private List<String> languagesSpoken;

    // Teaching Experience
    private List<TeachingExperience> teachingExperience;

    // Research Interests
    private List<String> researchInterests;

    // Professional Memberships
    private List<String> professionalMemberships;

    // Publications
    private List<Publication> publications;

    // Courses Taught
    private List<String> coursesTaught;

    // Special Accomplishments
    private List<String> specialAccomplishments;

    // Emergency Contact Information
    private EmergencyContact emergencyContact;

    // References
    private List<Reference> references;
    @JsonIgnore
    private boolean accountEnabled = false;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime createdDate = new ModelLocalDateTime(null);
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime updatedDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approveByAdminId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approveByAdminName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime confirmationDate;

}
