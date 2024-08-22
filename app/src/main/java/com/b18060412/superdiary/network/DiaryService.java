package com.b18060412.superdiary.network;

import com.b18060412.superdiary.network.responses.DiaryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DiaryService {
    @POST("record/daily/123456")
    Call<DiaryResponse> getDiaryData(
            @Query("start_time") String start_time,
            @Query("end_time") String end_time
    );
}
