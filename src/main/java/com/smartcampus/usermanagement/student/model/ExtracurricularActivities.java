package com.smartcampus.usermanagement.student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtracurricularActivities {
    private List<String> clubs;        // List of clubs or organizations the student is a part of
    private List<String> sports;       // List of sports or athletic teams the student participates in
    private List<String> hobbies;      // Hobbies or interests the student is involved in
    private String leadershipRoles;    // Any leadership roles held within clubs or organizations
    private String awards;             // Awards or recognitions received for extracurricular activities
    private String otherActivities;    // Any other extracurricular activities or involvements
}
