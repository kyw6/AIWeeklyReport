package com.b18060412.superdiary;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.network.DiaryService;
import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.responses.ApiResponse;
import com.b18060412.superdiary.network.responses.DiaryResponse;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivityNew extends AppCompatActivity {
    private Map<String, Calendar> map = new HashMap<>();//存放已经填写的日期，进行绘制
    private List<DiaryResponse> diaryList = new ArrayList<>();//存放已经填写的日记
    private TextView tvYearMonth;
    private CalendarView calendarView;//日历
    private TextView textViewReport;//周报展示区域
    private ImageView generateWeeklyReportButton;//生成周报按钮
    private ImageView headRightButton;//头部右侧按钮
    private ImageView startAddDiaryButton;//添加日记按钮
    //日报模块
    private FrameLayout diaryFrameLayout;
    private TextView textViewDiaryContent;
    private TextView textViewDiaryDate;
    private TextView textViewDiaryYearMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        textViewReport = findViewById(R.id.textView_weekly_report_right);
        generateWeeklyReportButton = findViewById(R.id.BTN_gene);
        headRightButton = findViewById(R.id.IV_head_right);
        startAddDiaryButton = findViewById(R.id.BTN_add);
        textViewDiaryContent = findViewById(R.id.textView_end);
        textViewDiaryDate = findViewById(R.id.textView_start_day);
        textViewDiaryYearMonth = findViewById(R.id.textView_start_year_month);
        diaryFrameLayout = findViewById(R.id.frameLayout_diary);


        initTopText();//初始化顶部文字显示
        initReportText();//初始化周报展示区域
        initCalendar();//初始化日历，进行绘制已经填写的日报
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
                // 将选中的日期格式化为 YYYY-MM-DD 格式
                String selectedDate = String.format("%04d-%02d-%02d", year, month, day);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                // 在 diaryList 中查找对应的日记
                DiaryResponse diaryResponse = null;
                for (DiaryResponse diary : diaryList) {
                    try {
                        // 解析 DiaryResponse 中的日期
                        Date date = inputFormat.parse(diary.getDate());
                        // 转换为 YYYY-MM-DD 格式
                        String diaryDate = outputFormat.format(date);

                        // 与选中的日期比较
                        if (selectedDate.equals(diaryDate)) {
                            diaryResponse = diary;
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // 显示选中的日期
//                textViewReport.setText("选中了" + year + "年" + month + "月" + day + "日");

                // 如果找到了对应的日记，显示其内容
                if (diaryResponse != null) {
                    //显示日记模块
                    showDiaryModule();
                    // 显示日记内容
                    textViewDiaryContent.setText(diaryResponse.getContent());
                    //显示日记日期
                    textViewDiaryDate.setText(day + "");
                    textViewDiaryYearMonth.setText(year + "年" + month + "月");
                } else {
                    // 如果没有找到对应的日记，则隐藏日报模块
                    hideDiaryModule();
                }
            }
        });


    }

    private void initCalendar() {
        getDataList();//获取网络数据，获取到之后进行绘制
    }

    /**
     * 这个方法接收日记的日期 (date) 作为参数，并返回一个 SchemeCalendar 对象。
     *
     * @return
     */
    private com.haibin.calendarview.Calendar getSchemeCalendar(String dateString) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        Log.d("kyw", "onBindViewHolder: " + dateString);
        // 提取年月日字段
        OffsetDateTime dateTime = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String year = String.valueOf(dateTime.getYear());
        String month = String.format("%d", dateTime.getMonthValue());
        String day = String.format("%d", dateTime.getDayOfMonth());
        calendar.setYear(Integer.parseInt(year));
        calendar.setMonth(Integer.parseInt(month));
        calendar.setDay(Integer.parseInt(day));
        calendar.addScheme(new com.haibin.calendarview.Calendar.Scheme());
        return calendar;
    }

    private void updateYearMonthText(int year, int month) {
        String yearMonth = year + "年" + month + "月";
        tvYearMonth.setText(yearMonth);
    }

    //发起网络请求，获取日记数据，保存到diaryList里面
    private void getDataList() {
        DiaryService diaryService = RetrofitClient.getClient().create(DiaryService.class);
        String startTime = "2020-01-01";
        String endTime = "2029-01-01";
        Call<ApiResponse<DiaryResponse>> call = diaryService.getDiaryData(startTime, endTime);
        call.enqueue(new Callback<ApiResponse<DiaryResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<DiaryResponse>> call, Response<ApiResponse<DiaryResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<DiaryResponse> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        diaryList = apiResponse.getData();
                        Log.d("kyw", "onResponse: " + diaryList.size());

                        for (DiaryResponse item : diaryList) {
                            map.put(getSchemeCalendar(item.getDate()).toString(), getSchemeCalendar(item.getDate()));
                        }
                        calendarView.setSchemeDate(map);

                    } else {
                        NetworkConnectionError();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DiaryResponse>> call, Throwable t) {
                NetworkConnectionError();
            }
        });
    }

    private void NetworkConnectionError() {
        Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
    }

    //显示日记模块
    private void showDiaryModule() {
        diaryFrameLayout.setVisibility(View.VISIBLE);
        textViewDiaryContent.setVisibility(View.VISIBLE);
        textViewDiaryDate.setVisibility(View.VISIBLE);
        textViewDiaryYearMonth.setVisibility(View.VISIBLE);
    }

    //隐藏日记模块
    private void hideDiaryModule() {
        diaryFrameLayout.setVisibility(View.GONE);
        textViewDiaryContent.setVisibility(View.GONE);
        textViewDiaryDate.setVisibility(View.GONE);
        textViewDiaryYearMonth.setVisibility(View.GONE);
    }
}