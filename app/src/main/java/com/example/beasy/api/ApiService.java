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
     * Sends a registration request to the server.
     * Returns the raw HTTP response body since backend response
     * structure is not strictly defined or mapped to a model.
     */
    @POST("api/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);
}