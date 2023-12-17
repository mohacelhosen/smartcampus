package com.smartcampus.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContact {
    private String name;            // Full name of the emergency contact
    private String relationship;    // Relationship to the person (e.g., parent, spouse, sibling)
    private String email;           // Email address of the emergency contact
    private String phone;           // Phone number of the emergency contact
    private String address;         // Address of the emergency contact (optional)
    private String nidNumber;
}

