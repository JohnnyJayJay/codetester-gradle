package com.github.johnnyjayjay.codetester.gradle.tasks;

import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Zip;

import java.io.File;


public abstract class ZipSource extends Zip {

    {
        setDescription("Creates a zip archive of the project's source code");
        setGroup("CodeTester");
        var mainSource = getProject()
                .getExtensions()
                .getByType(SourceSetContainer.class)
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        from(mainSource.getAllSource());
        getArchiveBaseName().set(getProject().getName() + "-source");
        getArchiveClassifier().set("zip");
        var destination = new File(getProject().getBuildDir(), "sourceDists");
        getDestinationDirectory().set(destination);
    }

}
