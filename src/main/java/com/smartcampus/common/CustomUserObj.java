package com.smartcampus.common;

import com.smartcampus.security.model.CustomUserDetails;

public class CustomUserObj {
    public static CustomUserDetails customObj(String academicId, String email, String fullName, String role){
        CustomUserDetails user = new CustomUserDetails();
        user.setAcademicId(academicId);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role);
        return user;
    }
}
