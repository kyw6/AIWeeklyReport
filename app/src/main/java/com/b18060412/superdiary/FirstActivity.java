package com.b18060412.superdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.util.PreferenceKeys;
import com.b18060412.superdiary.util.PreferencesUtil;

public class FirstActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
//        imageView = findViewById(R.id.start_button);
        //做伪登录
        fackLogin();
        //跳转到mainActivity
        Intent intent = new Intent(FirstActivity.this,MainActivityNew.class);
        startActivity(intent);
        finish();
    }

    private void fackLogin() {
        Log.d("kyw_FirstActivity", "fackLogin");
        // 初始化 PreferencesUtil
        PreferencesUtil.init(this);
        // 检查是否已经保存了 UUID
        String userUuid = PreferencesUtil.getString(PreferenceKeys.USER_UUID_KEY, null);
        if (userUuid == null) {
            // 如果 UUID 不存在，则生成新的 UUID 并保存
            userUuid = generateUuid();
            PreferencesUtil.setString(PreferenceKeys.USER_UUID_KEY, userUuid);
            Log.d("kyw_FirstActivity", "首次登录，生成并保存 UUID: " + userUuid);
        } else {
            // 如果 UUID 已存在，直接使用
            Log.d("kyw_FirstActivity", "用户已登录，UUID: " + userUuid);
        }
    }
    // 生成 UUID (这里使用时间戳)
    private String generateUuid() {
        return String.valueOf(System.currentTimeMillis());
    }

}
