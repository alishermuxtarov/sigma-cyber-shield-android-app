package com.sigma.caller;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("config/")  // Replace with your actual endpoint
    Call<Map<String, ResponseItem>> getData();
}
