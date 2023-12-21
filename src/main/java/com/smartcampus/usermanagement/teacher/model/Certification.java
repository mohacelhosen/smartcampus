package com.smartcampus.usermanagement.teacher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification {
    private String name;            // Name of the certification or qualification
    private String issuingOrganization; // Organization that issued the certification
    private String issuanceDate;    // Date the certification was issued
    private String expirationDate;  // Date when the certification expires (if applicable)
    private String description;      // Description or details about the certification
    private String certificateUploadId;
}

