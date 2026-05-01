package com.example.beasy.api;

import com.example.beasy.model.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ApiService is an interface that describes our backend endpoints.
 * Retrofit reads this interface and automatically builds the code to make HTTP requests.
 */
public interface ApiService {

    /**
     *
     * @Body tells Retrofit to serialize RegisterRequest as JSON and put it in the request body.
     * Call<String> means the server will respond with a plain String
     */
    @POST("api/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);
}