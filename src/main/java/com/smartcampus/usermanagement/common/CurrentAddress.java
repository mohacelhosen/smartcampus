package com.smartcampus.usermanagement.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAddress {
	private String district;
	private String policeStation;
	private String postalCode;
	private String address;
}
