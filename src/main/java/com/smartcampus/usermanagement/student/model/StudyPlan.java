package com.smartcampus.usermanagement.student.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyPlan {
    private Integer academicYear;
    private String nameOfTheProgramme;
    private String departmentName;
    private String departmentShortForm;
    private String departmentCode; // it must have a 3-digit code
    private Semester semester; // fall, winter, summer
}
