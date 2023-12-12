package com.smartcampus.library.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class LibraryCardInformation {
	@Id
	private String id;
	private String academicId;
	private String departmentName;
	private BorrowingBook borrowName;
	private String email;
	private String contactNumber;
	private String cardNumber; // Library card number
	private LocalDate issueDate; // Date when the library card was issued
	private LocalDate expirationDate; // Date when the library card expires
	private String libraryName; // Name of the library
	private String libraryLocation; // Location or branch of the library
	private String borrowingPrivileges; // Details about the student's borrowing privileges
	private String fines; // Record of any fines or fees associated with the library card

}
