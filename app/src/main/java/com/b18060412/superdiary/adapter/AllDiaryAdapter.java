package com.b18060412.superdiary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b18060412.superdiary.R;

import java.util.List;

public class AllDiaryAdapter extends RecyclerView.Adapter<AllDiaryAdapter.DiaryViewHolder>{
    private List<DiaryItem> DiaryList;
    public AllDiaryAdapter(List<DiaryItem> diaryList) {
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
        DiaryItem diaryItem = DiaryList.get(position);
        holder.textView.setText(diaryItem.getText());

    }



    @Override
    public int getItemCount() {
        return DiaryList.size();
    }

    static class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView_end);
        }
    }
}
