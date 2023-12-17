package com.smartcampus.semester.model;

import com.smartcampus.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAndTeacherAndClass {
    private Course course;
    private String teacherName;
    private String teacherEmail;
    private String teacherContactNumber;
    private String classJoinCode;
}
