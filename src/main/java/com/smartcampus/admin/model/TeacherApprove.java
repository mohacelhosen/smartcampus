package com.smartcampus.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherApprove {
    private String teacherRegistrationId;
    private String teacherId;
    private String roles;
    private String sectionName;
    private String sectionCode;
    private String approveStatus;
    private Boolean accountEnabled;
    private String approveByAdminId;
    private String approveByAdminName;
}
