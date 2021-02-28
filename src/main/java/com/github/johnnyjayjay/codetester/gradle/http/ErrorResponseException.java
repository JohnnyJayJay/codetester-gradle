package com.github.johnnyjayjay.codetester.gradle.http;

public class ErrorResponseException extends RuntimeException {

    private final int code;

    public ErrorResponseException(int code, String message) {
        super("Received code " + code + ": " + message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
