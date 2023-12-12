package com.smartcampus.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String exceptionInfo;
	private String apiEndPoint;

	public AlreadyExistsException() {
	}

	public AlreadyExistsException(String exceptionInfo) {
		super(exceptionInfo);
	}

	public AlreadyExistsException(String exceptionInfo, String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
		this.exceptionInfo = exceptionInfo;
	}

}
