package com.smartcampus.Student.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class StudentBillingAndFinancialInformation {
	@Id
	private String id;
	private String studentID; // Student's unique identifier or ID
	private String billingAddress; // Billing address for the student
	private String accountBalance; // Current account balance or outstanding charges
	private String paymentHistory; // Record of payments and transactions
	private List<String> tuitionFees; // Breakdown of tuition and fees
	private List<String> scholarships; // Scholarships or financial aid received
	private String financialAidStatus; // Status of financial aid applications
	private String paymentDueDate; // Due date for tuition and fee payments
	private String paymentMethods; // Accepted payment methods (e.g., credit card, bank transfer)
	private String financialHoldStatus; // Status of any financial holds or restrictions
}
