package com.smartcampus.Student.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "USER")
public class UserInfo {
	@Id
	private String id;
	private String userName;
	private String userEmail;
}
