package com.smartcampus.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicAdvisor {
    private String advisorName;      // Full name of the academic advisor
    private String department;       // Academic department of the advisor
    private String contactNumber;    // Contact number of the advisor
    private String email;            // Email address of the advisor
    private String officeLocation;   // Location of the advisor's office
    private String advisingHours;    // Office hours or availability for advising
}

