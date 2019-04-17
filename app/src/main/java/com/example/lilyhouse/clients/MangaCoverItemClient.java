package com.example.lilyhouse.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MangaCoverItemClient {

    private static final String BASIC_JSON_URL = "https://m.dmzj.com/classify/";
    private static volatile Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (MangaCoverItemClient.class) {
                Gson gson = new GsonBuilder().setLenient().create();
                retrofit = new Retrofit.Builder().baseUrl(BASIC_JSON_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
        }
        return retrofit;
    }
}
