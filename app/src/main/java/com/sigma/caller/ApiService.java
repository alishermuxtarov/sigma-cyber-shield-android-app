package com.sigma.caller;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/keywords/?format=json")  // Replace with your actual endpoint
    Call<List<KeywordItem>> getKeywords();

    @GET("api/hosts/?format=json")  // Replace with your actual endpoint
    Call<List<HostItem>> getHosts();
}
