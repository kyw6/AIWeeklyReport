package com.b18060412.superdiary;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;

public class GenerateWeeklyReportActivity extends AppCompatActivity {
    private TextView tvYearMonth;
    private CalendarView calendarView;//日期
    //写一个map，用于存储选中的日期
    private Map<String, Calendar> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        initTopText();//初始化顶部文字显示
        initSelectedDate();//初始化日期选择
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

    private void updateYearMonthText(int year, int month) {
        String yearMonth = year + "年" + month + "月";
        tvYearMonth.setText(yearMonth);
    }

    private void initSelectedDate() {
        // 设置日期选择监听器
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean b) {
                // 当选中日期时回调
                calendarView.setSchemeDate(map);
                //输出map，用Log 输出
                Log.e("kyw", map.toString());
                Log.e("kyw", "当前选中的日期：" + calendarView.getSelectedCalendar().toString());
                calendarView.setSelectedColor(Color.RED, Color.BLUE, Color.GREEN);
                //多选怎么做

            }


        });
    }
}
