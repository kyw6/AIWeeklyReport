package com.b18060412.superdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

        // 设置输入框的提示文本
        input.setHint("请输入邮箱前缀");

        // 设置输入框的内边距（左右边距为16dp，上下边距为8dp）
        int paddingLeft = (int) (24 * getResources().getDisplayMetrics().density);
        int paddingTop = (int) (16 * getResources().getDisplayMetrics().density);
        int paddingRight = (int) (16 * getResources().getDisplayMetrics().density);
        int paddingBottom = (int) (4 * getResources().getDisplayMetrics().density);
        input.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);


        // 将EditText添加到AlertDialog中
        builder.setView(input);

        // 设置确定按钮及其点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取用户输入的文本
                String userInput = input.getText().toString().trim();

                // 检查输入是否为空
                if (userInput.isEmpty()) {
                    // 显示提示消息
                    Toast.makeText(MyPageActivity.this, "邮箱前缀不能为空", Toast.LENGTH_SHORT).show();
                    // 重新显示对话框
                    dialog.dismiss();
                    okrClick(); // 重新显示对话框
                } else {
                    // 在这里处理用户输入
                    Intent intent = new Intent(MyPageActivity.this, OKRActivity.class);
                    intent.putExtra("username", userInput);
                    startActivity(intent);
                }
            }
        });

        // 设置取消按钮及其点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了取消按钮
                dialog.dismiss(); // 关闭对话框
            }
        });

        // 创建并显示弹窗
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}