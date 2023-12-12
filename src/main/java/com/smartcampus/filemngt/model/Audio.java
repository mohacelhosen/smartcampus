package com.smartcampus.filemngt.model;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audio {
	private String title;
	private InputStream stream;
}
