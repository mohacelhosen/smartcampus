package com.smartcampus.usermanagement.teacher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;
import com.smartcampus.usermanagement.common.User;
import com.smartcampus.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "Teacher")
public class Teacher extends User {
    @Id
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String teacherAcademicId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String roles;
    private String designation;
    private List<EducationalQualification> educationalQualifications;
    private List<Certification> certifications;
    private List<Course> teachingSubjects;
    private List<String> languagesSpoken;
    private List<TeachingExperience> teachingExperience;
    private List<String> researchInterests;
    private List<String> professionalMemberships;
    private List<Publication> publications;
    private List<String> coursesTaught;
    private List<String> specialAccomplishments;
    private EmergencyContact emergencyContact;
    private List<Reference> references;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String applicationStatus;

    // Performance Metrics
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double teachingRating;  // Example: Rating out of 5
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer feedbackCount; // Number of feedback received
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String lastEvaluation; // Last evaluation date or type of evaluation

    // Availability
    private String availabilitySchedule; // Weekly availability, e.g., Mon-Fri 9am-5pm
    private List<String> preferredTeachingHours; // Preferred hours for teaching

    // Salary - Compensation
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double monthlySalary; // Monthly salary amount
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String compensationDetails; // Compensation breakdown or details

    // Additional Metadata
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department; // Department or subject area
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String tenureStatus; // Tenure status, e.g., "Tenured", "Probationary"
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String employmentType; // Full-time, Part-time, Contract

    @JsonIgnore
    private boolean accountEnabled = false;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime createdDate = new ModelLocalDateTime(null);
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime updatedDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime confirmationDate;
}
