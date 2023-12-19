package com.smartcampus.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentApprove {
    private String registrationId;
    private String applicationStatus;
    private String activateBy;
    private String semesterTerm2Digit;
    private String departmentCode3Digit;
    private Integer semesterNumber;
}
