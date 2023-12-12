package com.smartcampus.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IllegalArgument extends IllegalArgumentException {
	private static final long serialVersionUID = 5790755579279421165L;
	private String exceptionInfo;
	private String apiEndPoint;

	public IllegalArgument() {
	}

	public IllegalArgument(String exceptionInfo) {
		super(exceptionInfo);
	}

	public IllegalArgument(String exceptionInfo, String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
		this.exceptionInfo = exceptionInfo;
	}
}
