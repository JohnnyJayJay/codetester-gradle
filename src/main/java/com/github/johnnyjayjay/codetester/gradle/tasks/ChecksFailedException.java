package com.github.johnnyjayjay.codetester.gradle.tasks;

public class ChecksFailedException extends RuntimeException {

    public ChecksFailedException(String msg) {
        super(msg);
    }
}
