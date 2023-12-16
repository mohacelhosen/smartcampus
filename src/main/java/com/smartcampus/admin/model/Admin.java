package com.smartcampus.admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
{
	"userId": "string",
	"email": "string",
	"fullName": "string",
	"role": "string"
}
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Admin {
	@Id
	private String id;
	private String userId;
	private String email;
	private String fullName;
	private String role;
	private String password;
	private String creationTime;
}
