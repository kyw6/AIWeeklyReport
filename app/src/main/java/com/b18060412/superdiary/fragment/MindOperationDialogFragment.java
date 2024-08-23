package com.b18060412.superdiary.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.b18060412.superdiary.databinding.DialogOperateMindBinding;
import com.b18060412.superdiary.entity.Mind;
import com.gyso.treeview.TreeViewEditor;
import com.gyso.treeview.model.NodeModel;

public class MindOperationDialogFragment extends DialogFragment {

    public static final String TAG = "MindOperationDialogFrag";

    private NodeModel<Mind> node;
    private TreeViewEditor editor;
    DialogOperateMindBinding binding;

    public MindOperationDialogFragment(NodeModel<Mind> node, TreeViewEditor editor) {
        this.node = node;
        this.editor = editor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogOperateMindBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.llAddNodeContainer.setOnClickListener(clickedView -> {
            editor.addChildNodes(node, new NodeModel<Mind>(new Mind("请编辑内容")));
            dismiss();
        });
        binding.llRemoveNodeContainer.setOnClickListener(childView -> {
            editor.removeNode(node);
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
