package com.apartment.www.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class ApiError {
    private int status;
    private String message;
    private String timestamp;
    private Map<String, String> errors;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now().toString();
    }

    public ApiError(int status, String message, Map<String, String> errors) {
        this(status, message);
        this.errors = errors;
    }
}
