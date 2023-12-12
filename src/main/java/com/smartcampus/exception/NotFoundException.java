package com.smartcampus.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;
	private String exceptionInfo;
	private String apiEndPoint;

	public NotFoundException() {
	}

	public NotFoundException(String exceptionInfo) {
		super(exceptionInfo);
	}

	public NotFoundException(String exceptionInfo, String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
		this.exceptionInfo = exceptionInfo;
	}
}
