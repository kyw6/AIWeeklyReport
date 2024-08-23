package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class MyPageActivity extends AppCompatActivity {
    private ImageView backButton;//头部右侧按钮
    private TextView lookAllWeeklyReport;//查看所有周报
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        backButton = findViewById(R.id.IV_back_button);
        lookAllWeeklyReport = findViewById(R.id.TV_all_report);


        backButton.setOnClickListener(v -> finish());
        lookAllWeeklyReport.setOnClickListener(v -> {
            Intent intent = new Intent(MyPageActivity.this, AllDiaryActivity.class);
            startActivity(intent);
        });
    }
}