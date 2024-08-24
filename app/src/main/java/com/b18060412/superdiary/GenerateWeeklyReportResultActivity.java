package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateWeeklyReportResultActivity extends AppCompatActivity {
    private ImageView jump_to_mind;
    private LinearLayout loadingLayout;
    private LinearLayout contentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report_result);
        jump_to_mind = findViewById(R.id.jump_mind);
        jump_to_mind.setOnClickListener(v -> {
            Intent intent = new Intent(GenerateWeeklyReportResultActivity.this, MindActivity.class);
            startActivity(intent);
        });

        loadingLayout = findViewById(R.id.loading_layout);
        contentLayout = findViewById(R.id.content_layout);

        // 模拟网络请求
        loadData();
    }


    private void loadData() {
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
        contentLayout.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        // 在这里设置请求到的数据到你的UI元素上
        // 比如更新TextView, RecyclerView等
    }

}
