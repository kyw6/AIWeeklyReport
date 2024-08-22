package com.b18060412.superdiary;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


public class MainActivityNew extends AppCompatActivity {
    private TextView tvYearMonth;
    private CalendarView calendarView;//日期
    private TextView textViewReport;//周报展示区域
    private ImageView generateWeeklyReportButton;//生成周报按钮
    private ImageView headRightButton;//头部右侧按钮
    private ImageView startAddDiaryButton;//添加日记按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        textViewReport = findViewById(R.id.textView_weekly_report);
        generateWeeklyReportButton = findViewById(R.id.BTN_gene);
        headRightButton = findViewById(R.id.IV_head_right);
        startAddDiaryButton = findViewById(R.id.BTN_add);
        initTopText();//初始化顶部文字显示
        initReportText();//初始化周报展示区域
        // 设置生成周报按钮点击事件
        generateWeeklyReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityNew.this, GenerateWeeklyReportActivity.class);
                startActivity(intent);
//                overridePendingTransition(0, 0); // 禁用动画效果
            }
        });
        // 设置头部右侧按钮点击事件
        headRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityNew.this, MyPageActivity.class);
                startActivity(intent);

            }
        });

        startAddDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityNew.this, AddDiaryActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化顶部文字显示
    private void initTopText() {
        // 初始化 TextView 显示当前月份
        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();
        updateYearMonthText(year, month);

        // 设置月份变化监听器
        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                updateYearMonthText(year, month);
            }
        });
    }

    //初始化周报展示区域
    private void initReportText() {
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
                // 当选中的日期超出范围时回调
            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean b) {
                // 当日期被选中时回调
                int year = calendar.getYear();
                int month = calendar.getMonth();
                int day = calendar.getDay();
                textViewReport.setText("选中了" + year + "年" + month + "月" + day + "日");
            }
        });


    }

    private void updateYearMonthText(int year, int month) {
        String yearMonth = year + "年" + month + "月";
        tvYearMonth.setText(yearMonth);
    }
}