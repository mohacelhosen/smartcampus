package com.smartcampus.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException {
	private String exceptionCode;
	private String exceptionInfo;
	private String apiEndPoint;
	private LocalDateTime timeStamp;
}
