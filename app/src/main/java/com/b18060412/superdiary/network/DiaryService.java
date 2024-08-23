package com.b18060412.superdiary.network;

import com.b18060412.superdiary.network.responses.ApiResponse;
import com.b18060412.superdiary.network.responses.DiaryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DiaryService {
    @GET("record/daily/123456")
    Call<ApiResponse<DiaryResponse>> getDiaryData(
            @Query("start_time") String start_time,
            @Query("end_time") String end_time
    );
}
