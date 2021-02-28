package com.github.johnnyjayjay.codetester.gradle.http.model;

public class FailedCompilerOutput implements CheckResponse {

    private final String output;

    public FailedCompilerOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }
}
