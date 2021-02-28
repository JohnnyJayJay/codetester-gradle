package com.github.johnnyjayjay.codetester.gradle.http.model;

import java.util.List;
import java.util.OptionalLong;

public class CheckResult {

    private final String check;
    private final ResultType result;
    private final String message;
    private final List<LineResult> output;
    private final String errorOutput;
    private final Long durationMillis;

    public CheckResult(String check,
                       ResultType result,
                       String message,
                       List<LineResult> output,
                       String errorOutput,
                       Long durationMillis) {
        this.check = check;
        this.result = result;
        this.message = message;
        this.output = output;
        this.errorOutput = errorOutput;
        this.durationMillis = durationMillis;
    }

    public String getCheck() {
        return check;
    }

    public ResultType getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public List<LineResult> getOutput() {
        return output;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public OptionalLong getDurationMillis() {
        return durationMillis == null
                ? OptionalLong.empty()
                : OptionalLong.of(durationMillis);
    }

    public enum ResultType {
        /**
         * The check was successful.
         */
        SUCCESSFUL,
        /**
         * The check failed.
         */
        FAILED,
        /**
         * The check was not applicable to the file.
         */
        NOT_APPLICABLE
    }

}
