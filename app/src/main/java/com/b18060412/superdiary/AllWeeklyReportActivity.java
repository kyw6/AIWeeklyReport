package com.b18060412.superdiary;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.b18060412.superdiary.adapter.AllDiaryAdapter;
import com.b18060412.superdiary.network.DiaryService;
import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.responses.ApiResponse;
import com.b18060412.superdiary.network.responses.DiaryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllWeeklyReportActivity extends AppCompatActivity {
    private AllDiaryAdapter adapter;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private List<DiaryResponse> diaryList = new java.util.ArrayList<>();
    private ImageView backButton;//头部返回按钮
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_weekly_report);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.IV_back_button);

        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDataList();//获取数据


        backButton.setOnClickListener(v -> finish());
    }
    private void getDataList() {

        fetchVerificationCode();
        Log.d("kyw", "getDataList: "+diaryList.size());
    }
    private void fetchVerificationCode() {

        DiaryService diaryService = RetrofitClient.getClient().create(DiaryService.class);
        String startTime = "2024-07-14";
        String endTime = "2024-09-14";
        Call<ApiResponse<DiaryResponse>> call = diaryService.getDiaryData(startTime,endTime);
        call.enqueue(new Callback<ApiResponse<DiaryResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<DiaryResponse>> call, Response<ApiResponse<DiaryResponse>> response) {
                if (response.isSuccessful()){
                    ApiResponse<DiaryResponse> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null){
                        diaryList = apiResponse.getData();
                        Log.d("kyw", "onResponse: "+diaryList.size());
                        adapter = new AllDiaryAdapter(diaryList);
                        recyclerView.setAdapter(adapter);
                    }else {
                        NetworkConnectionError();
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<DiaryResponse>> call, Throwable t) {
                NetworkConnectionError();
            }
        });
    }
    private void NetworkConnectionError(){
        diaryList.clear();
        diaryList.add(new DiaryResponse("0","网络错误"));
        adapter = new AllDiaryAdapter(diaryList);
        recyclerView.setAdapter(adapter);
    }
}
