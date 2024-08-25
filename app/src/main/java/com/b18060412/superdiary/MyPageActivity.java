package com.b18060412.superdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MyPageActivity extends AppCompatActivity {
    private ImageView backButton;//头部右侧按钮
    private TextView lookAllWeeklyReport;//查看所有周报
    private TextView okrTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        backButton = findViewById(R.id.IV_back_button);
        lookAllWeeklyReport = findViewById(R.id.TV_all_report);
        okrTv = findViewById(R.id.tv_okr);

        backButton.setOnClickListener(v -> finish());
        lookAllWeeklyReport.setOnClickListener(v -> {
            Intent intent = new Intent(MyPageActivity.this, AllDiaryActivity.class);
            startActivity(intent);
        });
        okrTv.setOnClickListener(v -> {
            //弹框
            okrClick();
        });
    }

    private void okrClick() {
        // 创建AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 设置弹窗的标题
        builder.setTitle("请输入小米邮箱前缀");

        // 创建一个EditText作为输入框
        final EditText input = new EditText(this);

        // 将EditText添加到AlertDialog中
        builder.setView(input);

        // 设置确定按钮及其点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取用户输入的文本
                String userInput = input.getText().toString();
                // 在这里处理用户输入
                Intent intent = new Intent(MyPageActivity.this, OKRActivity.class);
                intent.putExtra("email", userInput);
                startActivity(intent);
            }
        });

        // 设置取消按钮及其点击事件（可选）
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了取消按钮
            }
        });

        // 创建并显示弹窗
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}