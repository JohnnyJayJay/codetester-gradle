package com.github.johnnyjayjay.codetester.gradle.http.model;

import java.util.List;
import java.util.Map;

public class FailedCompilerOutput implements CheckResponse {

    private final Map<String, List<String>> diagnostics;
    private final String output;

    public FailedCompilerOutput(Map<String, List<String>> diagnostics, String output) {
        this.diagnostics = diagnostics;
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public Map<String, List<String>> getDiagnostics() {
        return diagnostics;
    }

}
