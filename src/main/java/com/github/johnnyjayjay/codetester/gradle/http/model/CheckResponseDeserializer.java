package com.github.johnnyjayjay.codetester.gradle.http.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CheckResponseDeserializer implements JsonDeserializer<CheckResponse> {
    @Override
    public CheckResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();
        return context.deserialize(json, object.has("output")
                ? FailedCompilerOutput.class
                : SubmissionCheckResult.class);
    }
}
