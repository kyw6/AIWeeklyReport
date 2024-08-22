package com.b18060412.superdiary;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.b18060412.superdiary.adapter.AllDiaryAdapter;
import com.b18060412.superdiary.adapter.DiaryItem;

import java.util.List;

public class AllWeeklyReportActivity extends AppCompatActivity {
    private AllDiaryAdapter adapter;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private List<DiaryItem> diaryList = new java.util.ArrayList<>();
    private ImageView backButton;//头部返回按钮
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_weekly_report);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.IV_back_button);

        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDataList();//获取数据
        adapter = new AllDiaryAdapter(diaryList);
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> finish());
    }
    private void getDataList() {
        diaryList.add(new DiaryItem("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"));
        diaryList.add(new DiaryItem("2222222222222222222"));
        diaryList.add(new DiaryItem("3333333333333333333"));
        diaryList.add(new DiaryItem("11111111111111111111"));
        diaryList.add(new DiaryItem("2222222222222222222"));
        diaryList.add(new DiaryItem("3333333333333333333"));
        diaryList.add(new DiaryItem("11111111111111111111"));
        diaryList.add(new DiaryItem("2222222222222222222"));
        diaryList.add(new DiaryItem("3333333333333333333"));
        diaryList.add(new DiaryItem("11111111111111111111"));
        diaryList.add(new DiaryItem("2222222222222222222"));
        diaryList.add(new DiaryItem("3333333333333333333"));
        Log.d("kyw", "getDataList: "+diaryList.size());
    }
}
