package com.github.johnnyjayjay.codetester.gradle.http.model;

public class LineResult {

    private final Type type;
    private final String content;

    public LineResult(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("[%-7s] %s", type, content);
    }

    public enum Type {
        PARAMETER,
        ERROR,
        INPUT,
        OUTPUT,
        OTHER
    }

}
