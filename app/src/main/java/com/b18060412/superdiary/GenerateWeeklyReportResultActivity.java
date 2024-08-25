package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.network.RetrofitClient;
import com.b18060412.superdiary.network.WeeklyReportService;
import com.b18060412.superdiary.network.responses.ApiResponseNotList;
import com.b18060412.superdiary.network.responses.WeekReportResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//生成周报结果页面
public class GenerateWeeklyReportResultActivity extends AppCompatActivity {
    private static final String TAG = "kyw_GWResultActivity";
    private TextView tvHeadShowTime;//顶部时间文字
    private LinearLayout loadingLayout;
    private String startTimeStr = null;//用户选择的起始时间，格式为2024-08-23
    private String endTimeStr = null;//用户选择的结束时间
    private String uuid = null;//用户id

    private EditText et_content;
    private ImageView backButton;
    private ImageView moreButton;//更多按钮
    private ImageView jump_to_mind;//思维导图
    private ImageView doneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_weekly_report_result);

        loadingLayout = findViewById(R.id.loading_layout);
        et_content = findViewById(R.id.et_content);
        tvHeadShowTime = findViewById(R.id.tv_head_show_time);
        backButton = findViewById(R.id.ic_back);
        moreButton = findViewById(R.id.ic_more);
        doneButton = findViewById(R.id.ic_nike);

        Intent dataIntent = getIntent();
        startTimeStr = dataIntent.getStringExtra("start_time_str");
        endTimeStr = dataIntent.getStringExtra("end_time_str");
        uuid = dataIntent.getStringExtra("uuid");

        jump_to_mind = findViewById(R.id.jump_mind);
        jump_to_mind.setOnClickListener(v -> {
            Intent jumpIntent = new Intent(GenerateWeeklyReportResultActivity.this, MindActivity.class);
            jumpIntent.putExtra("start_time_str", startTimeStr);
            jumpIntent.putExtra("end_time_str", endTimeStr);
            jumpIntent.putExtra("uuid", uuid);
            startActivity(jumpIntent);
        });

        // 网络请求，获取周报
        if (startTimeStr == null || endTimeStr == null || uuid == null) {
            Log.d(TAG, "start_time_str end_time_str uuid 为空");
            finish();
        } else {
            loadData();//请求周报数据
            initTvHeadShowTime();//顶部文字初始化
        }
        backButton.setOnClickListener(v -> finish());
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(GenerateWeeklyReportResultActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                moreButton.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.GONE);
            }
        });
    }


    private void loadData() {
        // 显示 Loading 页面
        showLoading();
        getWeeklyReportData(startTimeStr, endTimeStr, uuid);//发起网络请求

    }

    private void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        et_content.setVisibility(View.GONE);
    }

    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
        et_content.setVisibility(View.VISIBLE);
    }


    private void getWeeklyReportData(String start_time, String end_time, String uuid) {

        // 创建一个WeeklyReportService实例
        WeeklyReportService weeklyReportService = RetrofitClient.getClient().create(WeeklyReportService.class);
        // 发起网络请求
        Call<ApiResponseNotList<WeekReportResponse>> call = weeklyReportService.getWeeklyReportData(start_time, end_time, uuid);
        call.enqueue(new Callback<ApiResponseNotList<WeekReportResponse>>() {
            @Override
            public void onResponse(Call<ApiResponseNotList<WeekReportResponse>> call, Response<ApiResponseNotList<WeekReportResponse>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    ApiResponseNotList<WeekReportResponse> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        WeekReportResponse weekReportResponse = apiResponse.getData();
                        et_content.setText(Html.fromHtml(weekReportResponse.getContent(), Html.FROM_HTML_MODE_LEGACY));
//                        et_content.setText(weekReportResponse.getContent());
                        Log.d(TAG, "获取周报成功-周报id" + weekReportResponse.getWrID());
                    } else {
                        Log.d(TAG, "获取周报失败111");
                        Toast.makeText(GenerateWeeklyReportResultActivity.this, "获取周报失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "HTTP 状态码: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseNotList<WeekReportResponse>> call, Throwable t) {
                hideLoading();
                Log.d(TAG, "获取周报失败222");
                Log.d(TAG, "网络请求失败: " + t.getMessage());
                Toast.makeText(GenerateWeeklyReportResultActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void initTvHeadShowTime() {
        String result;
        // 分割开始日期和结束日期
        String[] startParts = startTimeStr.split("-");
        String[] endParts = endTimeStr.split("-");

        // 获取月、日
        String startMonth = startParts[1];
        String startDay = startParts[2];
        String endMonth = endParts[1];
        String endDay = endParts[2];

        // 去除前导零
        startMonth = removeLeadingZero(startMonth);
        endMonth = removeLeadingZero(endMonth);
        startDay = removeLeadingZero(startDay);
        endDay = removeLeadingZero(endDay);

        // 生成最终的格式化字符串
        result = String.format("%s月%s日 - %s月%s日周报", startMonth, startDay, endMonth, endDay);
        tvHeadShowTime.setText(result);
    }

    // 辅助方法去除前导零
    private static String removeLeadingZero(String str) {
        if (str.startsWith("0")) {
            return str.substring(1);
        }
        return str;
    }

    // 显示弹出菜单
    private void showPopupMenu(View view) {

        // 创建 PopupMenu 对象
        PopupMenu popupMenu = new PopupMenu(this, view);

        // 加载菜单资源
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.weekly_report_popup_menu, popupMenu.getMenu());

        // 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        Toast.makeText(GenerateWeeklyReportResultActivity.this, "进入修改模式", Toast.LENGTH_SHORT).show();
                        // 使 EditText 可编辑
                        et_content.setFocusable(true);
                        et_content.setFocusableInTouchMode(true);
                        et_content.setClickable(true);
                        et_content.setEnabled(true); // 确保 EditText 可以编辑并响应用户输入
                        moreButton.setVisibility(View.GONE);
                        doneButton.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.action_delete:
                        Toast.makeText(GenerateWeeklyReportResultActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // 显示菜单
        popupMenu.show();
    }
}
