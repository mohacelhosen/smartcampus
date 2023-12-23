package com.smartcampus.classroom.model;

import com.smartcampus.common.ModelLocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notes {
	private String institutionCode;
	private String noteId;
	private String noteTitle;
	private ModelLocalDateTime uploadedNoteDateTime = new ModelLocalDateTime(null);
}
