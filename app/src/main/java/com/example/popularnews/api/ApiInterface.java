package com.example.popularnews.api;

import com.example.popularnews.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(
        @Query("country") String country,
        @Query("apiKey") String apiKey
    );
}
