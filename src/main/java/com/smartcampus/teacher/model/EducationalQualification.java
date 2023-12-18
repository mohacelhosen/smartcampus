package com.smartcampus.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalQualification {
    private String degree;          // Academic degree (e.g., Bachelor's, Master's, Ph.D.)
    private String fieldOfStudy;    // Field of study or major
    private String institution;     // Educational institution where the qualification was earned
    private String completionYear;  // Year of qualification completion
    private String honors;          // Any honors or distinctions (optional)
    private String eduQualDocumentId; // Shortened name for educational qualification document upload ID
}
