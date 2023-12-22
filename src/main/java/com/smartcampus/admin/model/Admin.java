package com.smartcampus.admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.smartcampus.usermanagement.common.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Admin  extends User{
	@Id
	private String id;
	private String roles;
	private String academicId;
}
