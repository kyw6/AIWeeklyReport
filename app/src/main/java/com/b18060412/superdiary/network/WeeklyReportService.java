package com.b18060412.superdiary.network;


import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.WeekReportResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

//周报生成接口
public interface WeeklyReportService {
    @FormUrlEncoded
    @POST("record/summarize")
    Call<ApiResponseNotList<WeekReportResponse>> getWeeklyReportData(
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("uuid") String uuid

    );
}
