package com.b18060412.superdiary;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.b18060412.superdiary.util.MyDateStringUtil;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddDiaryActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private String selectDay = "";
    private String selectMonth = "";
    private String selectYear = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        // 初始化线程池
        executorService = Executors.newSingleThreadExecutor();

        // 初始化UI元素
        TextView tvDate = findViewById(R.id.tv_date);
        ImageView avatar = findViewById(R.id.avatar);
        ImageButton btnCancel = findViewById(R.id.btn_cancel);
        EditText etReport = findViewById(R.id.et_report);



        Intent intent = getIntent();
        if (intent != null) {
            selectDay = intent.getStringExtra("day");
            selectMonth = intent.getStringExtra("month");
            selectYear = intent.getStringExtra("year");
            Log.d("kyw","传输过来的是：" +selectDay + " " +selectMonth + " " + selectYear);
        }

        // 获取用户选中的日期并设置到TextView中
        tvDate.setText(MyDateStringUtil.formatDateToChinese(selectDay,selectMonth,selectYear));

        // 设置按钮点击事件
        btnCancel.setOnClickListener(v -> finish());

        ImageButton btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            String date = tvDate.getText().toString();
            String content = etReport.getText().toString();
            executorService.execute(() -> sendDataToServer(date, content));
        });
    }
    private void sendDataToServer(String date, String content) {
        try {
            String userId = "123456";
            date = MyDateStringUtil.formatDateToTransfer(selectDay,selectMonth,selectYear);
            Log.d("kyw","选中的日期，格式化之后是：" + date);
            // 设置请求的URL
            URL url = new URL("http://101.43.134.112:8080/record/daily");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            connection.setDoOutput(true);

            // 定义 boundary
            String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

            // 准备要发送的form-data数据
            StringBuilder formData = new StringBuilder();

            formData.append("--").append(boundary).append("\r\n");
            formData.append("Content-Disposition: form-data; name=\"uuid\"\r\n\r\n");
            formData.append(userId).append("\r\n");

            formData.append("--").append(boundary).append("\r\n");
            formData.append("Content-Disposition: form-data; name=\"date\"\r\n\r\n");
            formData.append(date).append("\r\n");

            formData.append("--").append(boundary).append("\r\n");
            formData.append("Content-Disposition: form-data; name=\"content\"\r\n\r\n");
            formData.append(content).append("\r\n");

            formData.append("--").append(boundary).append("--\r\n");
// 输出 content 到日志
            Log.d("FormDataContent", "content: " + content);
            // 发送数据
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = formData.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 检查响应代码
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            runOnUiThread(() -> {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("ServerResponse", "发送成功，响应代码：" + responseCode + "，响应消息：" + responseMessage);
                    Toast.makeText(this, "添加日报成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ServerError", "发送失败，错误代码：" + responseCode + "，错误消息：" + responseMessage);
                    Toast.makeText(this, "发送失败，错误代码：" + responseCode + "，错误消息：" + responseMessage, Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Log.e("SendDataError", "发送数据时出错：" + e.getMessage());

            // 显示在 UI 中
            runOnUiThread(() -> {
                Toast.makeText(this, "发送数据时出错：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }



//    private void sendDataToServer(String date, String content) {
//        try {
//            String userId = "5ceb8625-5e25-11ef-8315-00ff2ab7625f";
//            date  = "2024-08-20";
//            // 设置请求的URL
//            URL url = new URL("http://10.192.218.220:8080/record/daily");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json; utf-8");
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setDoOutput(true);
//
//            // 准备要发送的JSON数据
//            JSONObject jsonParam = new JSONObject();
//            jsonParam.put("uuid", userId);
//            jsonParam.put("date", date);
//
//            jsonParam.put("content", content);  // 这里将其作为 JSONObject 嵌入
//
//
//            // 打印组合后的JSON
//            String jsonString = jsonParam.toString(4); // 缩进4个空格，格式化输出
//            Log.d("JSONOutput", jsonString);
//            // 发送数据
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = jsonParam.toString().getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            // 检查响应代码
//            int responseCode = connection.getResponseCode();
//            String responseMessage = connection.getResponseMessage();
//            runOnUiThread(() -> {
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    Toast.makeText(this, "数据已发送成功！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.e("ServerError", "发送失败，错误代码：" + responseCode + "，错误消息：" + responseMessage);
//                    Toast.makeText(this, "发送失败，错误代码：" + responseCode + "，错误消息：" + responseMessage, Toast.LENGTH_LONG).show();
//                }
//            });
//
//        } catch (Exception e) {
//
//            Log.e("SendDataError", "发送数据时出错：" + e.getMessage());
//
//            // 显示在 UI 中
//            runOnUiThread(() -> {
//                Toast.makeText(this, "发送数据时出错：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            });
//        }
//    }

////    private void sendDataToServer(String date, String content) {
//        private void sendDataToServer(String date, String content) {
//            try {
//                String userId = "5ceb8625-5e25-11ef-8315-00ff2ab7625f";
//
//                // 设置请求的URL
//                URL url = new URL("http://10.192.218.220:8080/record/daily");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/json; utf-8");
//                connection.setRequestProperty("Accept", "application/json");
//                connection.setDoOutput(true);
//
//                // 准备要发送的JSON数据
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put("uuid", userId);
//                jsonParam.put("date", date);
//                jsonParam.put("content", content); // 将 content 作为字符串嵌入
//
//                // 发送数据
//                try (OutputStream os = connection.getOutputStream()) {
//                    byte[] input = jsonParam.toString().getBytes("utf-8");
//                    os.write(input, 0, input.length);
//                }
//
//                // 检查响应代码
//                int responseCode = connection.getResponseCode();
//                String responseMessage = connection.getResponseMessage();
//                runOnUiThread(() -> {
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        Toast.makeText(this, "数据已发送成功！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "发送失败，错误代码：" + responseCode + "，错误消息：" + responseMessage, Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                runOnUiThread(() ->
//                        Toast.makeText(this, "发送数据时出错：" + e.getMessage(), Toast.LENGTH_SHORT).show());
//            }
////        }
//
////        try {
////            String userId = "5ceb8625-5e25-11ef-8315-00ff2ab7625f";
////            content = "{\"done\":\"已完成\",\"doing\":\"正在做\",\"plan\":\"计划做\"}";
////            // 设置请求的URL
////            URL url = new URL("http://10.192.218.220:8080/record/daily");
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("POST");
////            connection.setRequestProperty("Content-Type", "application/json; utf-8");
////            connection.setRequestProperty("Accept", "application/json");
////            connection.setDoOutput(true);
////
////            // 准备要发送的JSON数据
////            JSONObject jsonParam = new JSONObject();
////            jsonParam.put("uuid", userId);
////            jsonParam.put("date", date);
//////            jsonParam.put("content", content);
////            jsonParam.put("content", new JSONObject(content));
////
////            // 发送数据
////            try (OutputStream os = connection.getOutputStream()) {
////                byte[] input = jsonParam.toString().getBytes("utf-8");
////                os.write(input, 0, input.length);
////            }
////
////            // 检查响应代码
////            int responseCode = connection.getResponseCode();
////            runOnUiThread(() -> {
////                if (responseCode == HttpURLConnection.HTTP_OK) {
////                    Toast.makeText(this, "数据已发送成功！", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(this, "发送失败，错误代码：" + responseCode, Toast.LENGTH_SHORT).show();
////                }
////            });
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            runOnUiThread(() ->
////                    Toast.makeText(this, "发送数据时出错：" + e.getMessage(), Toast.LENGTH_SHORT).show());
////        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}