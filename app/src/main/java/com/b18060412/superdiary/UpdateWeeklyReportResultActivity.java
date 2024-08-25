package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

    private EditText et_content;
    private ImageView backButton;
    private ImageView moreButton;//更多按钮


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

        et_content = findViewById(R.id.et_content);
        backButton = findViewById(R.id.ic_back);
        moreButton = findViewById(R.id.ic_more);
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
        backButton.setOnClickListener(v -> finish());
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
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
                        Toast.makeText(UpdateWeeklyReportResultActivity.this, "进入修改模式", Toast.LENGTH_SHORT).show();
                        // 使 EditText 可编辑
                        et_content.setFocusable(true);
                        et_content.setFocusableInTouchMode(true);
                        et_content.setClickable(true);
                        et_content.setEnabled(true); // 确保 EditText 可以编辑并响应用户输入
                        return true;
                    case R.id.action_delete:
                        Toast.makeText(UpdateWeeklyReportResultActivity.this, "删除", Toast.LENGTH_SHORT).show();
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
