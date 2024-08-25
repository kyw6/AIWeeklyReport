package com.b18060412.superdiary.network;


import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.WeekReportResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

//周报相关
public interface WeeklyReportService {
    //周报生成接口
    @FormUrlEncoded
    @POST("record/summarize")
    Call<ApiResponseNotList<WeekReportResponse>> getWeeklyReportData(
            @Field("start_time") String start_time,
            @Field("end_time") String end_time,
            @Field("uuid") String uuid

    );

    //查看某一天的周报 TODO 修改路径
    @GET("record/week/{uuid}")
    Call<ApiResponseNotList<WeekReportResponse>> getWeeklyReportByDay(
            @Path("uuid") String uuid,
            @Query("start_time") String startTime,
            @Query("end_time") String endTime


    );

    //TODO 查看全部周报

}
