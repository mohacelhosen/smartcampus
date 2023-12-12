package com.smartcampus.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateModel {
	private String academicId;
	private String previousPassword;
	private String newPassword;
}
