package com.example.creditmarket.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public AppException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
