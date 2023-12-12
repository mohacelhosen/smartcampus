package com.smartcampus.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ByteArrayFile {
	private byte[] content;
	private String fileNameWithExtension;
	private String projectRelatedPath;
}
