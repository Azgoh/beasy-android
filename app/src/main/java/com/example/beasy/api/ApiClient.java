package com.example.beasy.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient is a singleton that creates and holds one Retrofit instance.
 * A singleton means we only ever create it ONCE and reuse it everywhere.
 */
public class ApiClient {

    // Spring Boot backend URL.
    // On Android emulator, 10.0.2.2 maps to your PC's localhost (127.0.0.1).
    // If you use a real device, replace with your PC's local IP e.g. "http://192.168.1.X:8080/"
    private static final String BASE_URL = "http://192.168.100.39:8080/"; // replace with your IP

    private static Retrofit retrofitInstance = null;

    // Private constructor so nobody can do: new ApiClient()
    private ApiClient() {}

    /**
     * Returns the single Retrofit instance, creating it if it doesn't exist yet.
     * "synchronized" makes this thread-safe (safe to call from multiple places at once).
     */
    public static synchronized Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // GsonConverterFactory automatically converts Java objects to JSON and back
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }
}