package com.smartcampus.filemngt.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "photos")
public class Photo {
	@Id
	private String id;

	private String fileName;
	private String uploadTime;
	private String fileSize;
	private Binary image;
}
