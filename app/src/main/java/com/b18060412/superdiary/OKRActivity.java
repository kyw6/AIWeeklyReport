package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.network.OkrService;
import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.responses.OkrResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OKRActivity extends AppCompatActivity {

    private TextView okrTextView;
    private TextView krsTextView;
    private TextView resTextView;
    private TextView proposalTextView;
    private ProgressBar loadingProgressBar;
    private TextView error_text_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okr);

        // 初始化UI控件
        okrTextView = findViewById(R.id.okr_text_view);
        krsTextView = findViewById(R.id.krs_text_view);
        resTextView = findViewById(R.id.res_text_view);
        proposalTextView = findViewById(R.id.proposal_text_view);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        error_text_view = findViewById(R.id.error_text_view);
        // 从RetrofitClient获取Retrofit实例
        Retrofit retrofit = RetrofitClient.getClient();

        // 创建OkrService实例
        OkrService okrService = retrofit.create(OkrService.class);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Log.d("mk_OkrActivity", "用户名：" + username);

        // 使用用户名发起请求
//        String username = "makang3";  // 替换为实际用户名
        Call<OkrResponse> call = okrService.getOkrByUserName(username);

        // 显示加载动画
        loadingProgressBar.setVisibility(View.VISIBLE);

        // 异步请求
        call.enqueue(new Callback<OkrResponse>() {
            @Override
            public void onResponse(Call<OkrResponse> call, Response<OkrResponse> response) {
                // 隐藏加载动画
                loadingProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // 请求成功，显示数据
                    Log.d("mk_OkrActivity", "请求成功：" + response.body().getData().toString());
                    OkrResponse okrResponse = response.body();
                    displayOkrData(okrResponse.getData());
                } else {
                    Log.e("mk_OkrActivity", "请求失败：" + response.message());
                    showErrorText();
                    Toast.makeText(OKRActivity.this, "网络错误" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OkrResponse> call, Throwable t) {
                // 隐藏加载动画
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(OKRActivity.this, "网络错误" , Toast.LENGTH_SHORT).show();
                showErrorText();
                Log.e("mk_OkrActivity", "网络请求失败", t);
            }
        });
    }

    private void displayOkrData(OkrResponse.Data data) {
        if (data != null) {
            okrTextView.setText("OKRs: " + formatList(data.getOkr()));
            krsTextView.setText("KRs: " + formatList(data.getKrs()));
            resTextView.setText("Res: " + data.getContent());  // 更新字段名
            proposalTextView.setText("Proposal: " + data.getPropose());  // 更新字段名

            // 显示数据
            okrTextView.setVisibility(View.VISIBLE);
            krsTextView.setVisibility(View.VISIBLE);
            resTextView.setVisibility(View.VISIBLE);
            proposalTextView.setVisibility(View.VISIBLE);
        } else {
            okrTextView.setText("OKRs: 无数据");
            krsTextView.setText("KRs: 无数据");
            resTextView.setText("Res: 无数据");
            proposalTextView.setText("Proposal: 无数据");

            // 显示数据
            okrTextView.setVisibility(View.VISIBLE);
            krsTextView.setVisibility(View.VISIBLE);
            resTextView.setVisibility(View.VISIBLE);
            proposalTextView.setVisibility(View.VISIBLE);
        }
    }

    // 辅助方法，用于格式化列表
    private String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "无";
        }
        StringBuilder formatted = new StringBuilder();
        for (String item : list) {
            formatted.append(item).append("\n");
        }
        return formatted.toString().trim();
    }

    void showErrorText(){
        error_text_view.setVisibility(View.VISIBLE);
    }
}
