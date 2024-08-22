package com.b18060412.superdiary.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b18060412.superdiary.R;
import com.b18060412.superdiary.network.responses.DiaryResponse;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AllDiaryAdapter extends RecyclerView.Adapter<AllDiaryAdapter.DiaryViewHolder>{
    private List<DiaryResponse> DiaryList;
    public AllDiaryAdapter(List<DiaryResponse> diaryList) {
        DiaryList = diaryList;
    }
    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        DiaryResponse diaryItem = DiaryList.get(position);
        if (diaryItem.getContent().equals("网络错误")){
            holder.textViewTimeDay.setText("0");
            holder.textViewTimeMonthYear.setText("0000" + "年" + "00" + "月");
            holder.textViewContent.setText("网络请求失败了~抱歉");
        }else {
            String dateString = diaryItem.getDate();
            Log.d("kyw", "onBindViewHolder: "+dateString);
            // 提取年月日字段
            OffsetDateTime dateTime = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String year = String.valueOf(dateTime.getYear());
            String month = String.format("%d", dateTime.getMonthValue());
            String day = String.format("%d", dateTime.getDayOfMonth());
            holder.textViewTimeDay.setText(day);
            holder.textViewTimeMonthYear.setText(year + "年" + month + "月");
            holder.textViewContent.setText(diaryItem.getContent());
        }


    }



    @Override
    public int getItemCount() {
        return DiaryList.size();
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        TextView textViewTimeDay;
        TextView textViewTimeMonthYear;

        DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textView_end);
            textViewTimeMonthYear = itemView.findViewById(R.id.textView_start_year_month);
            textViewTimeDay = itemView.findViewById(R.id.textView_start_day);
        }
    }
}
