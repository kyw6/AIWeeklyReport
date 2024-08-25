package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//修改周报
public class UpdateWeeklyReportResultActivity  extends AppCompatActivity {
    private static final String TAG = "kyw_UWResultActivity";
    //获取的数据
    private String weeklyReportTittle;
    private String weeklyReportStartDate;
    private String weeklyReportEndDate;
    private String weeklyReportContent;
    private String weeklyReportMind;
    //页面
    private TextView tittle;
    private EditText content;
    private ImageView jump_to_mind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weekly_report_result);
        //前面的界面传过来的数据

//        intent.putExtra("weeklyTittle", weeklyReportTittle);
//        intent.putExtra("weeklyReportStartDate", weeklyReportStartDate);
//        intent.putExtra("weeklyReportEndDate", weeklyReportEndDate);
//        intent.putExtra("weeklyReportContent", weeklyReportResponse.getContent());
//        intent.putExtra("weeklyReportMind", weeklyReportResponse.getMind());

        //获取数据
        Intent dataIntent = getIntent();
        weeklyReportTittle = dataIntent.getStringExtra("weeklyTittle");
        weeklyReportStartDate = dataIntent.getStringExtra("weeklyReportStartDate");
        weeklyReportEndDate = dataIntent.getStringExtra("weeklyReportEndDate");
        weeklyReportContent = dataIntent.getStringExtra("weeklyReportContent");
        weeklyReportMind = dataIntent.getStringExtra("weeklyReportMind");
        String uuid = "123456";
        //页面
        tittle = findViewById(R.id.tv_head_show_time);
        content = findViewById(R.id.et_content);
        jump_to_mind = findViewById(R.id.jump_mind);
        //赋值
        tittle.setText(weeklyReportTittle);
        content.setText(weeklyReportContent);

        //传给思维导图页面
        jump_to_mind.setOnClickListener(v -> {
            Intent jumpIntent = new Intent(UpdateWeeklyReportResultActivity.this, MindActivity.class);
            jumpIntent.putExtra("start_time_str", weeklyReportStartDate);
            jumpIntent.putExtra("end_time_str", weeklyReportEndDate);
            jumpIntent.putExtra("uuid", uuid);
            startActivity(jumpIntent);
        });

    }
}
