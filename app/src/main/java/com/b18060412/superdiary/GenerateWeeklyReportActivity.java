package com.b18060412.superdiary;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateWeeklyReportActivity extends AppCompatActivity {
    private TextView tvYearMonth;
    private CalendarView calendarView;//日期
    private TextView showText;
    //写一个map，用于存储选中的日期
    private Map<String, Calendar> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        showText = findViewById(R.id.textView_weekly_report);
        initTopText();//初始化顶部文字显示
        initSelectedDate();//初始化日期选择
        Log.d("kyw", "111 map.toString()" + map.toString());
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
        calendarView.setOnCalendarRangeSelectListener(new CalendarView.OnCalendarRangeSelectListener() {
            @Override
            public void onCalendarSelectOutOfRange(Calendar calendar) {
                // 用户选择了一个超出可选范围的日期
                Log.d("kyw", "超出");
            }

            @Override
            public void onSelectOutOfRange(Calendar calendar, boolean isClick) {
                // 处理超出范围的日期选择，可能要禁用一些选择或提示用户
                if (isClick) {
                    Log.d("kyw", "超出");
                } else {
                    // 处理非点击事件的超出范围逻辑
                    Log.d("kyw", "没超出");
                }
            }

            @Override
            public void onCalendarRangeSelect(Calendar calendar, boolean isClick) {
                // 处理用户选择了一个日期范围
                List<Calendar> selectedCalendars = calendarView.getSelectCalendarRange();
//                Calendar start = selectedCalendars.get(0); // 开始日期
//                Calendar end = selectedCalendars.get(selectedCalendars.size() - 1); // 结束日期
//                Toast.makeText(GenerateWeeklyReportActivity.this, "选择的日期范围: " + start.toString() + " 到 " + end.toString(), Toast.LENGTH_SHORT).show();
                Log.d("kyw", "选择的日期范围" + selectedCalendars.size());
                Log.d("kyw", "选择的日期范围" + selectedCalendars.toString());
                showText.setText(selectedCalendars.toString());

            }
        });


    }
}
