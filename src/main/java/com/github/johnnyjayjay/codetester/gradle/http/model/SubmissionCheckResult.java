package com.github.johnnyjayjay.codetester.gradle.http.model;

import com.github.johnnyjayjay.codetester.gradle.http.model.CheckResult.ResultType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SubmissionCheckResult implements CheckResponse {

    private final Map<String, List<CheckResult>> fileResults;

    public SubmissionCheckResult(Map<String, List<CheckResult>> fileResults) {
        this.fileResults = fileResults;
    }

    public Map<String, List<CheckResult>> getFileResults() {
        return fileResults;
    }

    public boolean overallSuccessful() {
        return fileResults.values().stream()
                .flatMap(Collection::stream)
                .map(CheckResult::getResult)
                .noneMatch(ResultType.FAILED::equals);
    }
}
