package com.github.johnnyjayjay.codetester.gradle.http;

import com.github.johnnyjayjay.codetester.gradle.http.model.Category;
import com.github.johnnyjayjay.codetester.gradle.http.model.CheckResponse;
import com.github.johnnyjayjay.codetester.gradle.http.model.CheckResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class CodeTesterClient {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(CheckResponse.class, new CheckResponseDeserializer())
            .create();

    private final CodeTesterService service;
    private final String username;
    private final String password;
    private final String refreshToken;

    private String accessToken;

    public CodeTesterClient(String baseUrl, String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        var retrofit =  new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .build();
        this.service = retrofit.create(CodeTesterService.class);
        this.refreshToken = login();
    }

    private <T> T attemptRequest(Function<String, Call<T>> action, boolean retry) throws IOException {
        var response = action.apply("Bearer " + accessToken).execute();
        if (response.code() == 401 && retry) {
            refreshAccessToken();
            return attemptRequest(action, false);
        } else if (!response.isSuccessful()) {
            try (ResponseBody error = response.errorBody()) {
                var message = new JsonParser()
                        .parse(error.string()).getAsJsonObject()
                        .get("error").getAsString();
                throw new ErrorResponseException(response.code(), message);
            }
        } else {
            return response.body();
        }
    }

    private String login() throws IOException {
        return attemptRequest((t) -> service.getRefreshToken(username, password), false).getToken();
    }

    private void refreshAccessToken() throws IOException {
        var token = attemptRequest((t) -> service.getAccessToken(refreshToken), false);
        this.accessToken = token.getToken();
    }

    public List<Category> getAllCategories() throws IOException {
        return attemptRequest(service::getAllCategories, true);
    }

    public CheckResponse testZipFile(long categoryId, File file) throws IOException {
        return attemptRequest((token) -> service.testZipFile(token, categoryId, file), true);
    }

}
