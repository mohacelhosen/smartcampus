package com.smartcampus.filemngt.model;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonFile {
	private String title;
	private InputStream stream;
	private String contentType;
}
