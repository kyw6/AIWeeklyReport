package com.b18060412.superdiary;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


public class MainActivityNew extends AppCompatActivity {
    private TextView tvYearMonth;
    private CalendarView calendarView;//日期
    private TextView textViewReport;//周报展示区域

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        textViewReport = findViewById(R.id.textView_weekly_report);
        initTopText();//初始化顶部文字显示
        initReportText();//初始化周报展示区域
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
                textViewReport.setText("今天是" + year + "年" + month + "月" + day + "日");
            }
        });


    }

    private void updateYearMonthText(int year, int month) {
        String yearMonth = year + "年" + month + "月";
        tvYearMonth.setText(yearMonth);
    }
}