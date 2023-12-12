package com.smartcampus.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TokenValidationException() {
	}

	public TokenValidationException(String message) {
		super(message);
	}
}
