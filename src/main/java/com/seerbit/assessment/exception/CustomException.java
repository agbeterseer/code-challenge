package com.seerbit.assessment.exception;


import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus status;
    private Throwable cause;

    public CustomException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public synchronized Throwable getCause() {
        return cause;
    }
}
