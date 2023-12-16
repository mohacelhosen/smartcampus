package com.smartcampus.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IncompleteDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncompleteDataException() {
	}

	public IncompleteDataException(String message) {
		super(message);
	}
}
