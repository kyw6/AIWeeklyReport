package com.b18060412.superdiary;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.network.DiaryService;
import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.WeeklyReportService;
import com.b18060412.superdiary.network.responses.ApiResponse;
import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.DiaryResponse;
import com.b18060412.superdiary.network.responses.WeekReportResponse;
import com.b18060412.superdiary.util.MyDateStringUtil;
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
    private ImageView generateWeeklyReportButton;//生成周报按钮
    private ImageView headRightButton;//头部右侧按钮
    private ImageView startAddDiaryButton;//添加日记按钮
    //日报模块
    private FrameLayout diaryFrameLayout;
    private TextView textViewDiaryContent;
    private TextView textViewDiaryDate;
    private TextView textViewDiaryYearMonth;
    //周报模块
    private FrameLayout weeklyReportFrameLayout;
    private TextView textViewWeeklyReportContent;
    private TextView textViewWeeklyReportTittle;

    private String selectDay = "";
    private String selectMonth = "";
    private String selectYear = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        tvYearMonth = findViewById(R.id.tv_year_month);
        calendarView = findViewById(R.id.CV_calendar);
        generateWeeklyReportButton = findViewById(R.id.BTN_gene);
        headRightButton = findViewById(R.id.IV_head_right);
        startAddDiaryButton = findViewById(R.id.BTN_add);
        textViewDiaryContent = findViewById(R.id.textView_end);
        textViewDiaryDate = findViewById(R.id.textView_start_day);
        textViewDiaryYearMonth = findViewById(R.id.textView_start_year_month);
        diaryFrameLayout = findViewById(R.id.frameLayout_diary);
        //周报模块
        weeklyReportFrameLayout = findViewById(R.id.frameLayout_weekly_report);//周报模块
        textViewWeeklyReportContent = findViewById(R.id.textView_weekly_report_right);//周报内容
        textViewWeeklyReportTittle = findViewById(R.id.textView_weekly_report_left);//周报标题

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
        // 设置添加日记按钮点击事件，如果有日记，则展示，否则就是添加
        startAddDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityNew.this, AddDiaryActivity.class);
                intent.putExtra("day", selectDay);
                intent.putExtra("month", selectMonth);
                intent.putExtra("year", selectYear);
                String str = MyDateStringUtil.formatDateToTransfer(selectDay, selectMonth, selectYear);
                String findKey = MyDateStringUtil.formatDateToServer(str);
                //遍历map，根据key值找到对应的value，即DiaryResponse对象
                //遍历diaryList，找到对应的日记，获取日记内容
                for (DiaryResponse diary : diaryList) {
                    if (diary.getDate().equals(findKey)) {
                        intent.putExtra("content", diary.getContent());
                        break;
                    }
                }
                startActivity(intent);
            }
        });
        // 设置日报模块点击事件：携带日历内容跳转到日记编辑页面
        diaryFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityNew.this, AddDiaryActivity.class);
                intent.putExtra("day", selectDay);
                intent.putExtra("month", selectMonth);
                intent.putExtra("year", selectYear);
                String str = MyDateStringUtil.formatDateToTransfer(selectDay, selectMonth, selectYear);
                String findKey = MyDateStringUtil.formatDateToServer(str);
                //遍历map，根据key值找到对应的value，即DiaryResponse对象
                //遍历diaryList，找到对应的日记，获取日记内容
                for (DiaryResponse diary : diaryList) {
                    if (diary.getDate().equals(findKey)) {
                        intent.putExtra("content", diary.getContent());
                        break;
                    }
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initCalendar();//初始化日历，重新发送请求，数据在list中。
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
                selectDay = String.valueOf(calendar.getDay());
                selectMonth = String.valueOf(calendar.getMonth());
                selectYear = String.valueOf(calendar.getYear());
                Log.d("kyw", "选中的日期是：" + selectDay + " " + selectMonth + " " + selectYear);

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
                // 用户点击日历某一天之后，查询那一天所在周的周报，没有的话就不展示周报模块，有就展示周报模块
                getWeeklyReport(MyDateStringUtil.formatDateToTransfer(selectDay, selectMonth, selectYear));
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
//        Log.d("kyw", "onBindViewHolder: " + dateString);
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
                        diaryList.clear();
                        diaryList = apiResponse.getData();
                        Log.d("kyw", "onResponse: " + diaryList.size());

                        for (DiaryResponse item : diaryList) {
                            //key key: 2024-08-20T00:00:00+08:00 value 2024-08-20T00:00:00+08:00
                            map.put(getSchemeCalendar(item.getDate()).toString(), getSchemeCalendar(item.getDate()));
                        }
                        //绘制日历Scheme
                        calendarView.setSchemeDate(map);
                        //刚添加完日历回来的话，显示日历模块
                        showBackDiary();

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
    //显示周报模块
    private void showWeekReportModule() {
        weeklyReportFrameLayout.setVisibility(View.VISIBLE);
        textViewWeeklyReportContent.setVisibility(View.VISIBLE);
        textViewWeeklyReportTittle.setVisibility(View.VISIBLE);
    }
    //隐藏周报模块
    private void hideWeekReportModule() {
        weeklyReportFrameLayout.setVisibility(View.GONE);
        textViewWeeklyReportContent.setVisibility(View.GONE);
        textViewWeeklyReportTittle.setVisibility(View.GONE);
    }


    //显示用户添加成功后，返回的日记模块，遍历日记list，找到用户添加的日记，显示到首页
    private void showBackDiary() {
        //这里解决首次进入app时，selectMonth selectDay selectYear为空，导致无法显示今天已经填写的日记模块
        if (TextUtils.isEmpty(selectMonth) || TextUtils.isEmpty(selectDay) || TextUtils.isEmpty(selectYear)){
            selectMonth = String.valueOf(calendarView.getCurMonth());
            selectDay = String.valueOf(calendarView.getCurDay());
            selectYear = String.valueOf(calendarView.getCurYear());
        }
        for (DiaryResponse item : diaryList) {
           String date = MyDateStringUtil.getFirstTenChars(item.getDate());
           String selectDate = MyDateStringUtil.formatDateToTransfer(selectDay, selectMonth, selectYear);
            //如果选中的日期 和 list中的日期相同，则显示日记模块
            if (date.equals(selectDate)) {
                // 显示选中的日期
                textViewDiaryContent.setText(item.getContent());
                //显示日记日期
                textViewDiaryDate.setText(selectDay);
                textViewDiaryYearMonth.setText(selectYear + "年" + selectMonth + "月");
                showDiaryModule();
                return;
            }
        }
        hideDiaryModule();
    }

    //获取选中的周报数据
    private void getWeeklyReport(String day){
        WeeklyReportService weeklyReportService = RetrofitClient.getClient().create(WeeklyReportService.class);
        String uuid = "123456";
        Call<ApiResponseNotList<WeekReportResponse>> call = weeklyReportService.getWeeklyReportByDay(day,uuid);
        call.enqueue(new Callback<ApiResponseNotList<WeekReportResponse>>() {
            @Override
            public void onResponse(Call<ApiResponseNotList<WeekReportResponse>> call, Response<ApiResponseNotList<WeekReportResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponseNotList<WeekReportResponse> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        //如果获取到数据，则显示数据，否则默认隐藏周报模块
                        showWeekReportModule();
                        textViewWeeklyReportContent.setText(apiResponse.getData().getContent());
                        textViewWeeklyReportTittle.setText("本周周报");
                    } else {
                        NetworkConnectionError();
                        hideWeekReportModule();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseNotList<WeekReportResponse>> call, Throwable t) {
                NetworkConnectionError();
                hideWeekReportModule();
            }
        });
    }

}