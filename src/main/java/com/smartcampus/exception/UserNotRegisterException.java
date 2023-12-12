package com.smartcampus.exception;

public class UserNotRegisterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotRegisterException() {
	}

	public UserNotRegisterException(String message) {
		super(message);
	}
}
