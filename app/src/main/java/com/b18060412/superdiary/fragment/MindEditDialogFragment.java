package com.b18060412.superdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.b18060412.superdiary.adapter.MindTreeViewAdapter;
import com.b18060412.superdiary.databinding.DialogEditMindBinding;
import com.b18060412.superdiary.entity.Mind;
import com.gyso.treeview.model.NodeModel;

public class MindEditDialogFragment extends DialogFragment {

    public static final String TAG = "MindEditDialogFragment";

    private NodeModel<Mind> node;
    private MindTreeViewAdapter adapter;

    private DialogEditMindBinding binding;

    public MindEditDialogFragment(NodeModel<Mind> node, MindTreeViewAdapter adapter) {
        this.node = node;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditMindBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.etNodeContent.setText(node.getValue().getContent());

        binding.llSaveNodeContainer.setOnClickListener(clickedView -> {
            node.getValue().setContent(binding.etNodeContent.getText().toString());
            adapter.notifyDataSetChange();
            dismiss();
        });
    }
}
