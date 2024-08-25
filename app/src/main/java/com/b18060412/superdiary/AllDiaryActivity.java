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
import com.b18060412.superdiary.util.PreferenceKeys;
import com.b18060412.superdiary.util.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//全部日报
public class AllDiaryActivity extends AppCompatActivity {
    String userUuid;
    private AllDiaryAdapter adapter;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private List<DiaryResponse> diaryList = new java.util.ArrayList<>();
    private ImageView backButton;//头部返回按钮
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_diary);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.IV_back_button);

        //获取uuid
        // 初始化 PreferencesUtil (如果尚未初始化)
        PreferencesUtil.init(AllDiaryActivity.this); // context 传入当前上下文，如 Activity.this 或 getApplicationContext()

        // 获取存储的 UUID
        userUuid = PreferencesUtil.getString(PreferenceKeys.USER_UUID_KEY, null);

        if (userUuid != null) {
            Log.d("kyw_OtherActivity", "获取到的 UUID: " + userUuid);
        } else {
            Log.d("kyw_OtherActivity", "UUID 未找到");
        }
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDataList();//获取数据


        backButton.setOnClickListener(v -> finish());
    }
    private void getDataList() {
        DiaryService diaryService = RetrofitClient.getClient().create(DiaryService.class);
        String startTime = "2024-07-14";
        String endTime = "2024-09-14";
        String uuid = userUuid;
        Call<ApiResponse<DiaryResponse>> call = diaryService.getDiaryData(uuid,startTime,endTime);
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
        Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
    }
}
