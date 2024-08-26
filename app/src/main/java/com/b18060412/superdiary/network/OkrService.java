package com.b18060412.superdiary.network;

import com.b18060412.superdiary.network.responses.OkrResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OkrService {
    //查看某一天的周报
    @GET("okr/{username}")
    Call<OkrResponse> getOkrByUserName(
            @Path("username") String username
    );
}
