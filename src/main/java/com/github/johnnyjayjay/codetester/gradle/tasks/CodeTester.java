package com.github.johnnyjayjay.codetester.gradle.tasks;

import com.github.johnnyjayjay.codetester.gradle.http.CodeTesterClient;
import com.github.johnnyjayjay.codetester.gradle.http.model.CheckResult;
import com.github.johnnyjayjay.codetester.gradle.http.model.CheckResult.ResultType;
import com.github.johnnyjayjay.codetester.gradle.http.model.FailedCompilerOutput;
import com.github.johnnyjayjay.codetester.gradle.http.model.LineResult;
import com.github.johnnyjayjay.codetester.gradle.http.model.SubmissionCheckResult;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class CodeTester extends DefaultTask {

    {
        setDescription("Sends a source code zip file to the CodeTester instance to run checks");
        setGroup("CodeTester");
    }

    @Input
    public abstract Property<String> getBaseUrl();

    @Input
    public abstract Property<String> getUsername();

    @Input
    public abstract Property<String> getPassword();

    @Input
    public abstract Property<String> getCategory();

    @InputFile
    public abstract RegularFileProperty getSourcesZip();

    @OutputDirectory
    public abstract DirectoryProperty getResultsDir();

    @TaskAction
    public void executeTask() throws IOException {
        var client = new CodeTesterClient(getBaseUrl().get(), getUsername().get(), getPassword().get());
        var categoryName = getCategory().get();
        System.out.printf("Looking for CodeTester category '%s'%n",  categoryName);
        var category = client.getAllCategories().stream()
                .filter((c) -> categoryName.equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Category '" + categoryName + "' could not be found"));
        System.out.printf("Checking %s with %s...%n", getSourcesZip().get().getAsFile().getName(), getBaseUrl().get());
        var response = client.testZipFile(category.getId(), getSourcesZip().get().getAsFile());
        if (response instanceof FailedCompilerOutput) {
            System.err.println(((FailedCompilerOutput) response).getOutput());
            throw new ChecksFailedException("Checks could not be run; compilation failed.");
        } else if (response instanceof SubmissionCheckResult) {
            var result = (SubmissionCheckResult) response;
            if (result.overallSuccessful()) {
                System.out.println("All checks passed.");
            } else {
                System.err.println("Some checks failed.");
                var appliedCheckResults = result.getFileResults()
                        .entrySet().stream()
                        .filter((entry) -> anyChecksRun(entry.getValue()))
                        .collect(Collectors.toList());
                for (var entry : appliedCheckResults) {
                    var fileName = entry.getKey();
                    var results = entry.getValue();
                    try (var output = new PrintStream(
                            getResultsDir().get().file(fileName + ".log").getAsFile(),
                            StandardCharsets.UTF_8)) {
                        log(output, fileName, results);
                    }
                    var failedResults = results.stream()
                            .filter((res) -> res.getResult() == ResultType.FAILED)
                            .collect(Collectors.toList());
                    log(System.err, fileName, failedResults);
                }
                throw new ChecksFailedException(
                        "Some checks failed; for the complete results, see the logs at " + getResultsDir().get()
                );
            }
        }
    }

    private boolean anyChecksRun(List<CheckResult> results) {
        return results.stream()
                .map(CheckResult::getResult)
                .anyMatch((res) -> res != ResultType.NOT_APPLICABLE);
    }

    private void log(PrintStream output, CheckResult result) {
        if (result.getResult() == ResultType.NOT_APPLICABLE) {
            return;
        }
        output.printf("Check '%s': %s%n", result.getCheck(), result.getResult());
        if (!result.getMessage().isEmpty()) {
            output.println(result.getMessage());
        }
        output.println("Interaction:");
        result.getOutput().stream()
                .map(LineResult::toString)
                .forEach(output::println);
    }

    private void log(PrintStream output, String file, List<CheckResult> results) {
        if (results.isEmpty()) {
            return;
        }
        output.printf("Results from %s%n", file);
        results.stream()
                .filter((res) -> res.getResult() != ResultType.NOT_APPLICABLE)
                .forEach((res) -> {
                    log(output, res);
                    output.println();
                });
    }


}
