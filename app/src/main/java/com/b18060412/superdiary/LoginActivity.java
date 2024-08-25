package com.b18060412.superdiary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;  // 原 edit_text_phone，现在假定用作邮箱输入
    private EditText editTextPassword;  // 原 edit_text_verification_code，现在假定用作密码输入
    private ImageButton buttonTogglePasswordVisibility;
    private Button buttonLogin;
    private TextView textRegisterNow;

    private boolean isPasswordVisible = false;  // 密码是否可见

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        editTextEmail = findViewById(R.id.edit_text_phone);  // 如果是用于邮箱输入，可以考虑将ID改为 edit_text_email
        editTextPassword = findViewById(R.id.edit_text_verification_code);
        buttonTogglePasswordVisibility = findViewById(R.id.button_toggle_password_visibility);
        buttonLogin = findViewById(R.id.button_login);
        textRegisterNow = findViewById(R.id.text_register_now);

        // 设置邮箱输入使用密码键盘
        editTextEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        // 设置状态栏颜色
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );

        handlePasswordVisibilityToggle();  // 处理密码可见性切换
        handleLoginButtonClick();  // 处理登录按钮点击事件

        // 添加文本改变监听器
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
    }

    // 处理密码可见性切换
    private void handlePasswordVisibilityToggle() {
        buttonTogglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    buttonTogglePasswordVisibility.setImageResource(R.drawable.ic_eye);  // 设置为关闭眼睛图标
                } else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    buttonTogglePasswordVisibility.setImageResource(R.drawable.ic_eye);  // 设置为打开眼睛图标
                }
                isPasswordVisible = !isPasswordVisible;
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });
    }

    // 处理登录按钮点击事件
    private void handleLoginButtonClick() {
        buttonLogin.setEnabled(false);  // 初始化为不可点击

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "请输入有效的邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 处理登录逻辑，跳转到主界面
                navigateToMainActivity();
            }
        });
    }

    // 文本改变监听器
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateInputs();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // 检查输入是否有效
    private void validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        boolean isEmailValid = isValidEmail(email);
        boolean isPasswordValid = !password.isEmpty();

        buttonLogin.setEnabled(isEmailValid && isPasswordValid);
    }

    // 验证邮箱格式
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 导航到主页面
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivityNew.class);
        startActivity(intent);
        finish();
    }
}