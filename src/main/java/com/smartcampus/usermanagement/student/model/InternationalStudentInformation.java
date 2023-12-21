package com.smartcampus.usermanagement.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternationalStudentInformation {
    private String visaStatus;         // Visa status of the international student
    private String passportNumber;     // Passport number of the international student
    private String countryOfOrigin;    // Country of origin or nationality
    private String nativeLanguage;     // Native language of the international student
    private String arrivalDate;        // Date of arrival to the host country/university
    private String housingArrangements; // Details about housing arrangements for international students
    private String orientationProgram;  // Participation in orientation programs for international students
    private String sponsorInformation;  // Information about the student's sponsor (if applicable)
    private String additionalRequirements; // Any additional requirements or documentation for international students
}

