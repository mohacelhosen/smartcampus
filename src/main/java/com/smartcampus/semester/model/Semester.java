package com.smartcampus.semester.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcampus.common.ModelLocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Semester {
    @Id
    private String id;
    private String departmentName;
    private String departmentCode;
    private Integer semesterNumber;
    private String semesterCode;
    private List<CourseAndTeacherAndClass> courseAndTeacherAndClasses;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime createdDateTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ModelLocalDateTime updateDateTime;
    private String createdBy;
}
