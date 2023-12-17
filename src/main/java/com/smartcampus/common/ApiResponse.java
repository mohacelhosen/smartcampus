package com.smartcampus.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data = null;
    private String timestamp = new ModelLocalDateTime(null).getLocalDateTimeStringAMPM();
    private String endpoint;
}
