package com.b18060412.superdiary.network;

import com.b18060412.superdiary.network.responses.ApiResponse;
import com.b18060412.superdiary.network.responses.DiaryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiaryService {
    @GET("record/daily/{uuid}")
    Call<ApiResponse<DiaryResponse>> getDiaryData(
            @Path("uuid") String uuid,
            @Query("start_time") String start_time,
            @Query("end_time") String end_time
    );
}
