package com.smartcampus.Student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipsAndFinancialAid {
    private String scholarshipName;     // Name of the scholarship or financial aid program
    private String scholarshipType;     // Type of scholarship (e.g., merit-based, need-based)
    private String awardAmount;         // Amount awarded or financial assistance
    private String awardYear;           // Year the scholarship was awarded
    private String applicationStatus;   // Status of scholarship application (e.g., pending, awarded)
    private String applicationDeadline; // Deadline for scholarship application
    private String awardDescription;    // Description of the scholarship or financial aid
}

