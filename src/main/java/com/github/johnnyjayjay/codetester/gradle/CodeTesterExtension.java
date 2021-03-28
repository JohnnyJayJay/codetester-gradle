package com.github.johnnyjayjay.codetester.gradle;

import org.gradle.api.provider.Property;

import java.time.Duration;

public abstract class CodeTesterExtension {

    public abstract Property<String> getBaseUrl();

    public abstract Property<String> getUsername();

    public abstract Property<String> getPassword();

    public abstract Property<String> getCategory();

    public abstract Property<Duration> getReadTimeout();

}
