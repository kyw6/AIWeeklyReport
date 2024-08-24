package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.WeeklyReportService;
import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.WeekReportResponse;
import com.b18060412.superdiary.util.MyDateStringUtil;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//生成周报
public class GenerateWeeklyReportActivity extends AppCompatActivity {
    private static final String TAG = "kyw_GWActivity";
    private TextView tvYearMonth;
    private CalendarView calendarView;//日期
    private TextView showText;
    private ImageView buttonGene;//生产周报按钮
    private List<Calendar> selectedCalendars = null;//选中的日期
    //写一个map，用于存储选中的日期
    private Map<String, Calendar> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        showText = findViewById(R.id.textView_weekly_report);
        buttonGene = findViewById(R.id.BTN_gene);
        initTopText();//初始化顶部文字显示
        initSelectedDate();//初始化日期选择
        initGenerateWeeklyReport();//初始化生成周报按钮
        Log.d(TAG, "111 map.toString()" + map.toString());
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
                Log.d(TAG, "超出");
            }

            @Override
            public void onSelectOutOfRange(Calendar calendar, boolean isClick) {
                // 处理超出范围的日期选择，可能要禁用一些选择或提示用户
                if (isClick) {
                    Log.d(TAG, "超出");
                } else {
                    // 处理非点击事件的超出范围逻辑
                    Log.d(TAG, "没超出");
                }
            }

            @Override
            public void onCalendarRangeSelect(Calendar calendar, boolean isClick) {
                // 处理用户选择了一个日期范围
                selectedCalendars = calendarView.getSelectCalendarRange();
                //selectedCalendars.toString() : [20240813,20240814,20240815]
                showText.setText(selectedCalendars.toString());

            }
        });
    }

    private void initGenerateWeeklyReport() {
        buttonGene.setOnClickListener(v -> {
            // 获取选中的日期范围
            if (selectedCalendars == null || selectedCalendars.isEmpty()) {
                Toast.makeText(this, "请先选择一段时间", Toast.LENGTH_SHORT).show();
            } else {
                // 获取头部元素
                String firstDay = selectedCalendars.get(0).getDay() + "";
                String firstDayMonth = selectedCalendars.get(0).getMonth() + "";
                String firstDayYear = selectedCalendars.get(0).getYear() + "";

                // 获取尾部元素
                String lastDay = selectedCalendars.get(selectedCalendars.size() - 1).getDay() + "";
                String lastDayMonth = selectedCalendars.get(selectedCalendars.size() - 1).getMonth() + "";
                String lastDayYear = selectedCalendars.get(selectedCalendars.size() - 1).getYear() + "";

                //传输选中的日期 给下个Activity
                Toast.makeText(this, "开始生成周报", Toast.LENGTH_SHORT).show();
                String uuid = "123456";
                String start_time_str = MyDateStringUtil.formatDateToTransfer(firstDay, firstDayMonth, firstDayYear);
                String end_time_str = MyDateStringUtil.formatDateToTransfer(lastDay, lastDayMonth, lastDayYear);
                Log.d(TAG, "start_time_str" + start_time_str + "end_time_str" + end_time_str);

                Intent intent = new Intent(GenerateWeeklyReportActivity.this, GenerateWeeklyReportResultActivity.class);
                intent.putExtra("start_time_str", start_time_str);
                intent.putExtra("end_time_str", end_time_str);
                intent.putExtra("uuid", uuid);
                Log.d(TAG, "intent" + intent);
                startActivity(intent);
                finish();
            }


        });
    }




}
