package com.smartcampus.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherApprove {
    private String teacherRegistrationId;
    private String role;
    private Boolean accountEnabled;
    private String applicationStatus;

    // Availability
    private String availabilitySchedule; // Weekly availability, e.g., Mon-Fri 9am-5pm
    private List<String> preferredTeachingHours; // Preferred hours for teaching

    // Salary - Compensation
    private Double monthlySalary; // Monthly salary amount
    private String compensationDetails; // Compensation breakdown or details

    // Additional Metadata
    private String department; // Department or subject area
    private String tenureStatus; // Tenure status, e.g., "Tenured", "Probationary"
    private String employmentType; // Full-time, Part-time, Contract
}
