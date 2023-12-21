package com.smartcampus.usermanagement.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeachingExperience {
    private String institution;       // Name of the educational institution where teaching occurred
    private String position;         // Teaching position (e.g., Lecturer, Assistant Professor)
    private String subject;          // Subject or course taught
    private String startDate;        // Start date of teaching experience
    private String endDate;          // End date of teaching experience
    private String description;      // Description or details about the teaching experience
}

