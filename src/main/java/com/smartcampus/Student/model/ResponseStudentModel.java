package com.smartcampus.Student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStudentModel {
    private List<StudentEntity> allStudent;
    private Integer total;
    private Integer skip;
    private Integer limit;
}