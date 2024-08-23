package com.b18060412.superdiary.network;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 基本域名硬编码到这里
    private static final String BASE_URL = "http://101.43.134.112:8080/";

    private static Retrofit retrofit = null;

//    private static final Gson GSON = new GsonBuilder()
//            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//            .create();

    private static final Gson GSON = new Gson();
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .build();
        }
        return retrofit;
    }

    public static Gson getGson() {
        return GSON;
    }
}
