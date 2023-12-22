package com.smartcampus.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<?> alreadyExist(AlreadyExistsException existsException) {
		CustomException exception = new CustomException();
		exception.setExceptionInfo(existsException.getExceptionInfo());
		exception.setExceptionCode("Exists p-101");
		exception.setTimeStamp(LocalDateTime.now());
		exception.setApiEndPoint(existsException.getApiEndPoint());
		return new ResponseEntity<>(exception, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(IllegalArgument.class)
	public ResponseEntity<?> illegalArgument(IllegalArgument illegalArgument) {
		CustomException exception = new CustomException();
		exception.setExceptionInfo(illegalArgument.getExceptionInfo());
		exception.setExceptionCode("IllegalArgument p-101");
		exception.setTimeStamp(LocalDateTime.now());
		exception.setApiEndPoint(illegalArgument.getApiEndPoint());
		return new ResponseEntity<>(exception, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> illegalArgument(NotFoundException notFoundException) {
		CustomException exception = new CustomException();
		exception.setExceptionInfo(notFoundException.getExceptionInfo());
		exception.setExceptionCode("Not Found Exception");
		exception.setTimeStamp(LocalDateTime.now());
		exception.setApiEndPoint(notFoundException.getApiEndPoint());
		return new ResponseEntity<>(exception, HttpStatus.NOT_ACCEPTABLE);
	}

//    security
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotRegisterException.class)
	public ResponseEntity<ExceptionModel> userNotRegister(UserNotRegisterException unre) {
		ExceptionModel model = new ExceptionModel();
		model.setCode("DB-101");
		model.setExceptionName(unre.getMessage());
		return new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
	}

	@ExceptionHandler(IncompleteUserInfoException.class)
	public ResponseEntity<ExceptionModel> userNotRegister(IncompleteUserInfoException incompleteUserInfo) {
		ExceptionModel model = new ExceptionModel();
		model.setCode("InCompleteUserInfo");
		model.setExceptionName(incompleteUserInfo.getMessage());
		return new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ExceptionModel> invalidRequest(InvalidRequestException invalidRequestException) {
		ExceptionModel model = new ExceptionModel();
		model.setCode("IR-404");
		model.setExceptionName(invalidRequestException.getMessage());
		return new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionModel> resourcesNotFound(ResourceNotFoundException resourceNotFoundException) {
		ExceptionModel model = new ExceptionModel();
		model.setCode("RNF-404");
		model.setExceptionName(resourceNotFoundException.getMessage());
		return new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
	}

	@ExceptionHandler(RegistrationFailedException.class)
	public ResponseEntity<ExceptionModel> registrationFailed(RegistrationFailedException registrationFailedException) {
		ExceptionModel model = new ExceptionModel();
		model.setCode("RNF-404");
		model.setExceptionName(registrationFailedException.getMessage());
		return new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler(TokenValidationException.class)
	public ResponseEntity<ExceptionModel> resourcesNotFound(TokenValidationException tokenValidationException) {
		ExceptionModel model = new ExceptionModel();
		model.setCode("TokenInvalid-404");
		model.setExceptionName(tokenValidationException.getMessage());
		return new ResponseEntity<>(model, HttpStatus.UNAUTHORIZED);
	}


}
