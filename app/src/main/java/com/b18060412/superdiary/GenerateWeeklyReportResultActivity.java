package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.WeeklyReportService;
import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.WeekReportResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//生成周报结果页面
public class GenerateWeeklyReportResultActivity extends AppCompatActivity {
    private static final String TAG = "kyw_GWResultActivity";
    private ImageView jump_to_mind;
    private LinearLayout loadingLayout;
    private EditText et_content;
    private String start_time_str = null;
    private String end_time_str = null;
    private String uuid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report_result);
        jump_to_mind = findViewById(R.id.jump_mind);
        jump_to_mind.setOnClickListener(v -> {
            Intent jumpIntent = new Intent(GenerateWeeklyReportResultActivity.this, MindActivity.class);
            startActivity(jumpIntent);
        });

        loadingLayout = findViewById(R.id.loading_layout);
        et_content = findViewById(R.id.et_content);

        Intent dataIntent = getIntent();
        start_time_str = dataIntent.getStringExtra("start_time_str");
        end_time_str = dataIntent.getStringExtra("end_time_str");
        uuid = dataIntent.getStringExtra("uuid");

        // 网络请求，获取周报
        Log.d(TAG, "start_time_str: " + start_time_str);
        if (start_time_str == null || end_time_str == null || uuid == null) {
            Log.d(TAG, "start_time_str end_time_str uuid 为空");
            finish();
        } else {
            loadData();
        }

    }


    private void loadData() {
        getWeeklyReportData(start_time_str, end_time_str, uuid);//发起网络请求

        // 显示 Loading 页面
        showLoading();

        // 模拟网络请求（可以替换为真实的网络请求代码）
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 网络请求完成，隐藏 Loading 页面
                hideLoading();

                // 显示请求到的数据
                showContent();
            }
        }, 3000); // 模拟3秒的网络请求
    }

    private void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        et_content.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
        et_content.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        // 在这里设置请求到的数据到你的UI元素上
        // 比如更新TextView, RecyclerView等
    }

    private void getWeeklyReportData(String start_time, String end_time, String uuid) {

        // 创建一个WeeklyReportService实例
        WeeklyReportService weeklyReportService = RetrofitClient.getClient().create(WeeklyReportService.class);
        // 发起网络请求
        Call<ApiResponseNotList<WeekReportResponse>> call = weeklyReportService.getWeeklyReportData(start_time, end_time, uuid);
        call.enqueue(new Callback<ApiResponseNotList<WeekReportResponse>>() {
            @Override
            public void onResponse(Call<ApiResponseNotList<WeekReportResponse>> call, Response<ApiResponseNotList<WeekReportResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponseNotList<WeekReportResponse> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        WeekReportResponse weekReportResponse = apiResponse.getData();
                        et_content.setText(weekReportResponse.getContent());
                        Log.d(TAG, "获取周报成功");
                    } else {
                        Log.d(TAG, "获取周报失败111");
                        Toast.makeText(GenerateWeeklyReportResultActivity.this, "获取周报失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "HTTP 状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseNotList<WeekReportResponse>> call, Throwable t) {
                Log.d("TAG", "获取周报失败222");
                Log.d("TAG", "网络请求失败: " + t.getMessage());
                Toast.makeText(GenerateWeeklyReportResultActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }


        });
    }

}
