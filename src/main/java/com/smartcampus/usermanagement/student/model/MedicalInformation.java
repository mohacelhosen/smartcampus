package com.smartcampus.usermanagement.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalInformation {
    private String medicalConditions;    // Any known medical conditions or allergies
    private String medications;          // Current medications the student is taking
    private String medicalHistory;       // Relevant medical history or past surgeries
    private String primaryCarePhysician; // Name of the student's primary care physician
    private String physicianContact;     // Contact information for the primary care physician
    private String healthInsurance;      // Information about the student's health insurance
    private String emergencyContact;     // Name and contact information of an emergency medical contact
    private String bloodType;            // Blood type of the student
}
