package com.b18060412.superdiary.network;


import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 基本域名硬编码到这里
    private static final String BASE_URL = "http://mini4-mirecord-test.g.mi.com/";
    //记得改add页面的
//    private static final String BASE_URL = "http://101.43.134.112:8080/";


    private static Retrofit retrofit = null;

//    private static final Gson GSON = new GsonBuilder()
//            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//            .create();

    private static final Gson GSON = new Gson();

    public static Retrofit getClient() {
        if (retrofit == null) {
            // 创建OkHttpClient并设置超时时间
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)  // 连接超时
                    .readTimeout(300, TimeUnit.SECONDS)     // 读取超时
                    .writeTimeout(300, TimeUnit.SECONDS)    // 写入超时
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)  // 设置自定义的OkHttpClient
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .build();
        }
        return retrofit;
    }

    public static Gson getGson() {
        return GSON;
    }
}
