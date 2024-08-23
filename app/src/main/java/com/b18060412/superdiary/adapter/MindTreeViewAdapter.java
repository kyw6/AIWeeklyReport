package com.b18060412.superdiary.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.b18060412.superdiary.R;
import com.b18060412.superdiary.databinding.NodeBaseLayoutBinding;
import com.b18060412.superdiary.entity.Mind;
import com.gyso.treeview.adapter.DrawInfo;
import com.gyso.treeview.adapter.TreeViewAdapter;
import com.gyso.treeview.adapter.TreeViewHolder;
import com.gyso.treeview.line.BaseLine;
import com.gyso.treeview.line.DashLine;
import com.gyso.treeview.model.NodeModel;

public class MindTreeViewAdapter extends TreeViewAdapter<Mind> {

    private DashLine dashLine =  new DashLine(Color.parseColor("#F06292"),6);
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public TreeViewHolder<Mind> onCreateViewHolder(@NonNull ViewGroup viewGroup, NodeModel<Mind> node) {
        NodeBaseLayoutBinding nodeBinding = NodeBaseLayoutBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new TreeViewHolder<>(nodeBinding.getRoot(), node);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeViewHolder<Mind> holder) {
        View itemView = holder.getView();
        NodeModel<Mind> node = holder.getNode();
        final Mind mind = node.value;

        TextView tv_item_content = itemView.findViewById(R.id.tv_item_content);
        tv_item_content.setText(mind.getContent() == null ? "" : mind.getContent());

        itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, node);
            }
        });
        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(v -> {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, node);
            }
            return true;
        });
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    @Override
    public BaseLine onDrawLine(DrawInfo drawInfo) {
        return null;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, NodeModel<Mind> node);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, NodeModel<Mind> node);
    }
}
