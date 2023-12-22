package com.smartcampus.classroom.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ClassAnnouncement {
    @Id
    private String id;
    private String institutionCode;
    private String classTitle;
    private String teacherName;
    private String teacherAcademicId;
    private String classStartSession;
    private String departmentCode;
    private Integer semesterNumber;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer announcementNumber;
    private String courseCode;

}
