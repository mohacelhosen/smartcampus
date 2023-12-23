package com.smartcampus.classroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {
	private String institutionCode;
	private String crName;
	private String crContactNumber;
}
