package com.github.johnnyjayjay.codetester.gradle;

import com.github.johnnyjayjay.codetester.gradle.tasks.CodeTester;
import com.github.johnnyjayjay.codetester.gradle.tasks.ZipSource;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class CodeTesterPlugin implements Plugin<Project> {

    // TODO: 27/02/2021 log destination dir config
    @Override
    public void apply(Project project) {
        var extension = project.getExtensions().create("codeTester", CodeTesterExtension.class);
        var zipTask = project.getTasks().register("zipSource", ZipSource.class, (task) -> {
            task.exclude("**/edu/kit/informatik/Terminal.java");
        });
        project.getTasks().register("codeTester", CodeTester.class, task -> {
            task.dependsOn(zipTask);
            task.getBaseUrl().set(extension.getBaseUrl().orElse("https://codetester.ialistannen.de/"));
            task.getUsername().set(extension.getUsername());
            task.getPassword().set(extension.getPassword());
            task.getCategory().set(extension.getCategory());
            task.getReadTimeout().set(extension.getReadTimeout().orElse(Duration.of(1, ChronoUnit.MINUTES)));
            task.getSourcesZip().set(zipTask.flatMap(ZipSource::getArchiveFile));
            task.getResultsDir().set(new File(project.getBuildDir(), "codeTester"));
        });
    }
}
