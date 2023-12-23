package com.smartcampus.classroom.model;

import com.smartcampus.common.ModelLocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Objective {
	private String institutionCode;
	private String title;
	private String description;
	private ModelLocalDateTime dateTime = new ModelLocalDateTime(null);
}
