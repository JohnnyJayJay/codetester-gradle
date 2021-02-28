package com.github.johnnyjayjay.codetester.gradle.http;

import com.github.johnnyjayjay.codetester.gradle.http.model.Category;
import com.github.johnnyjayjay.codetester.gradle.http.model.CheckResponse;
import com.github.johnnyjayjay.codetester.gradle.http.model.Token;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.io.File;
import java.util.List;

public interface CodeTesterService {

    MediaType MULTIPART_TYPE = MediaType.parse("application/zip");

    @FormUrlEncoded
    @POST("login")
    Call<Token> getRefreshToken(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login/get-access-token")
    Call<Token> getAccessToken(@Field("refreshToken") String refreshToken);

    @GET("check-category/get-all")
    Call<List<Category>> getAllCategories(@Header("Authorization") String bearerToken);

    @Multipart
    @POST("/test/zip/{categoryId}")
    Call<CheckResponse> testZipFile(
            @Header("Authorization") String bearerToken,
            @Path("categoryId") long categoryId,
            @Part MultipartBody.Part file
    );

    default Call<CheckResponse> testZipFile(String bearerToken, long categoryId, File file) {
        var fileBody = RequestBody.create(MULTIPART_TYPE, file);
        var part = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        return testZipFile(bearerToken, categoryId, part);
    }


}
