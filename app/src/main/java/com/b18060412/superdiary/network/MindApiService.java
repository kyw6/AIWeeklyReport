package com.b18060412.superdiary.network;

import com.b18060412.superdiary.entity.MindRequestParam;
import com.b18060412.superdiary.entity.Result;
import com.b18060412.superdiary.entity.WeekRecord;
import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.WeekReportResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MindApiService {

    // 获取最近的周报及思维导图
    @GET("/index/{uuid}")
    Call<Result<Map<String, WeekRecord>>> getMind(@Path("uuid") String uuid);

    @FormUrlEncoded
    @POST("/record/summarize")
    Call<Result<WeekRecord>> getWeekRecordSummarize(@Field("start_time") String startTime, @Field("end_time") String endTime, @Field("uuid") String uuid);

    //查看某一天的周报
    @GET("record/week/{uuid}")
    Call<Result<WeekRecord>> getWeeklyReport(
            @Path("uuid") String uuid,
            @Query("start_time") String startTime,
            @Query("end_time") String endTime
    );

    @FormUrlEncoded
    @PATCH("/record/summarize/mind")
    Call<Result> modifyMind(@Field("wrid") String wrid, @Field("uuid") String uuid, @Field("mind") String mind);
}
