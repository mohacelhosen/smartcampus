package com.smartcampus.usermanagement.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermanentAddress {
	private String district;
	private String policeStation;
	private String postalCode;
	private String village;
}
