package com.example.popularnews.api;

import com.example.popularnews.Utils;

import org.junit.Test;

import retrofit2.Retrofit;

import static com.example.popularnews.api.ApiClient.getApiClient;
import static org.junit.Assert.*;

public class ApiClientTest {
    @Test
    public void testApiClient() {
        Retrofit output = getApiClient();
        assertNotNull(output);
    }

}