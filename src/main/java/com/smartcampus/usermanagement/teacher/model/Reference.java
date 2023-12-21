package com.smartcampus.usermanagement.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reference {
    private String name;            // Full name of the reference
    private String organization;    // Organization or company where the reference works
    private String relationship;    // Relationship to the person (e.g., supervisor, colleague)
    private String email;           // Email address of the reference
    private String phone;           // Phone number of the reference
    private String address;         // Address of the reference (optional)
    private String nidNumber;
}
