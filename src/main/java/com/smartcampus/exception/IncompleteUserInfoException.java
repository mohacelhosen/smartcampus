package com.smartcampus.exception;

public class IncompleteUserInfoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IncompleteUserInfoException() {
	}

	public IncompleteUserInfoException(String message) {
		super(message);
	}
}
