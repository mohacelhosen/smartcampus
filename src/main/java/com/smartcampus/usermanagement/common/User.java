package com.smartcampus.usermanagement.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String registrationId;
    private String institutionName;
    private String institutionCode;
    private String firstName;
    private String lastName;
    private String nickName;
    private String contactNumber;
    private LocalDate dob;
    private String gender;
    private String nationality;
    private String religion;
    private String birthPlace;
    private String maritalStatus;
    private String bloodGroup;
    private String email;
    private CurrentAddress currentAddress;
    private PermanentAddress permanentAddress;
    private String photoUrl;
    private String fatherName;
    private String motherName;
}
