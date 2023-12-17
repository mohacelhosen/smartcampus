package com.smartcampus.teacher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.CurrentAddress;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.common.PermanentAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "Teacher")
public class Teacher {
    @Id
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String registrationId;
    private String teacherId;
    private String roles;
    private String designation;
    private String sectionName; // admission
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
    private String status;
    private String bloodGroup;
    private String email;
    private String nidNumber;
    private CurrentAddress currentAddress;
    private PermanentAddress permanentAddress;
    private Map<String, String> documents;

    // Educational Qualifications
    private List<EducationalQualification> educationalQualifications;

    // Certifications and Professional Development
    private List<Certification> certifications;

    // Teaching Subjects
    private List<String> teachingSubjects;

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

    private String password;
    @JsonIgnore
    private boolean isEnabled = false;
    private ModelLocalDateTime createdDate = new ModelLocalDateTime(null);
    private ModelLocalDateTime updatedDate;
}
