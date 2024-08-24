package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateWeeklyReportResultActivity extends AppCompatActivity {
    private ImageView jump_to_mind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report_result);
        jump_to_mind = findViewById(R.id.jump_mind);
        jump_to_mind.setOnClickListener(v -> {
            Intent intent = new Intent(GenerateWeeklyReportResultActivity.this, MindActivity.class);
            startActivity(intent);
        });
    }
}
