package com.b18060412.superdiary;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.b18060412.superdiary.adapter.MindTreeViewAdapter;
import com.b18060412.superdiary.databinding.ActivityMindBinding;
import com.b18060412.superdiary.entity.Mind;
import com.b18060412.superdiary.entity.Result;
import com.b18060412.superdiary.entity.ServerMind;
import com.b18060412.superdiary.entity.WeekRecord;
import com.b18060412.superdiary.fragment.MindEditDialogFragment;
import com.b18060412.superdiary.fragment.MindOperationDialogFragment;
import com.b18060412.superdiary.network.MindApiService;
import com.b18060412.superdiary.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyso.treeview.TreeViewEditor;
import com.gyso.treeview.adapter.TreeViewAdapter;
import com.gyso.treeview.layout.BoxRightTreeLayoutManager;
import com.gyso.treeview.layout.TreeLayoutManager;
import com.gyso.treeview.line.BaseLine;
import com.gyso.treeview.line.SmoothLine;
import com.gyso.treeview.listener.TreeViewControlListener;
import com.gyso.treeview.model.NodeModel;
import com.gyso.treeview.model.TreeModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MindActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMindBinding binding;
    private Handler handler = new Handler();
    private boolean enableDrag = false;
    private boolean showMind = false;
    private Gson gson = RetrofitClient.getGson();
    private Retrofit retrofit = RetrofitClient.getClient();
    private String wrid;
    private String uuid = "5ceb8625-5e25-11ef-8315-00ff2ab7625f";
    private String startTime = "2024-08-14";
    private String endTime = "2024-08-22";
    MindApiService api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initWidgets();
    }

    private void initWidgets() {
        api = retrofit.create(MindApiService.class);

        //1 customs adapter
        MindTreeViewAdapter adapter = new MindTreeViewAdapter();

        //2 configure layout manager; unit dp
        TreeLayoutManager treeLayoutManager = getTreeLayoutManager();

        //3 view setting
        binding.baseTreeView.setAdapter(adapter);
        binding.baseTreeView.setTreeLayoutManager(treeLayoutManager);

        //4 nodes data setting
        setData(adapter);

        //5 get an editor. Note: an adapter must set before get an editor.
        final TreeViewEditor editor = binding.baseTreeView.getEditor();

        //6 you own others jobs
        doYourOwnJobs(editor, adapter);
    }

    void doYourOwnJobs(TreeViewEditor editor, MindTreeViewAdapter adapter){
        binding.lTabBar.ibBack.setOnClickListener(view -> {
            this.finish();
        });

        binding.lTabBar.ibSwitch.setOnClickListener(view -> {
            switchView(adapter);
        });

        binding.lTabBar.ibSave.setOnClickListener(view -> {
            if (wrid != null) {
                api.modifyMind(wrid, uuid, getMindStructure(adapter.getTreeModel())).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
//                        if (response.body().getCode() == 0) {
//                            showToast("思维导图保存成功");
//                        }
                        showToast("思维导图保存成功");
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable throwable) {
                    }
                });
            }
        });

        //drag to move node
        binding.ivDragSwitch.setOnClickListener(view -> {
            enableDrag = !enableDrag;
            editor.requestMoveNodeByDragging(enableDrag);
            if (enableDrag) {
                binding.ivDragSwitch.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.iv_float_icon_selected, null));
            } else {
                binding.ivDragSwitch.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.iv_float_icon_unselected, null));
            }
        });

        //focus, means that tree view fill center in your window viewport
        binding.ivFocus.setOnClickListener(view -> {
            editor.focusMidLocation();
        });

        adapter.setItemClickListener((itemView, node)-> {
            if (node.isMarkThenReset()) {
                // double click
                itemView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.card_background_unselected, null));
            } else {
                editor.clearNodeMark();
                editor.unifyBackgrounds(ResourcesCompat.getDrawable(getResources(), R.drawable.card_background_unselected, null));
                node.mark();
                itemView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.card_background_selected, null));
            }
        });

        adapter.setItemLongClickListener((item, node)-> {
            if (!enableDrag) {
                editor.unifyBackgrounds(ResourcesCompat.getDrawable(getResources(), R.drawable.card_background_unselected, null));
                if (node.isMarkThenReset()) {
                    new MindEditDialogFragment(node, adapter).show(getSupportFragmentManager(), MindEditDialogFragment.TAG);
                } else {
                    editor.clearNodeMark();
                    new MindOperationDialogFragment(node, editor).show(getSupportFragmentManager(), MindOperationDialogFragment.TAG);
                }
            }
        });

        //treeView control listener
        final Object token = new Object();
        Runnable dismissRun = ()->{
            binding.scalePercent.setVisibility(View.GONE);
        };

        binding.baseTreeView.setTreeViewControlListener(new TreeViewControlListener() {
            @Override
            public void onScaling(int state, int percent) {
                Log.e(TAG, "onScaling: "+state+"  "+percent);
                binding.scalePercent.setVisibility(View.VISIBLE);
                if(state == TreeViewControlListener.MAX_SCALE){
                    binding.scalePercent.setText("MAX");
                }else if(state == TreeViewControlListener.MIN_SCALE){
                    binding.scalePercent.setText("MIN");
                }else{
                    binding.scalePercent.setText(percent+"%");
                }
                handler.removeCallbacksAndMessages(token);
                handler.postAtTime(dismissRun,token,SystemClock.uptimeMillis()+2000);
            }

            @Override
            public void onDragMoveNodesHit(NodeModel<?> draggingNode, NodeModel<?> hittingNode, View draggingView, View hittingView) {
                Log.e(TAG, "onDragMoveNodesHit: draging["+draggingNode+"]hittingNode["+hittingNode+"]");
            }
        });
    }

    private TreeLayoutManager getTreeLayoutManager() {
        int space_50dp = 30;
        int space_20dp = 20;
        BaseLine line = getLine();
        return new BoxRightTreeLayoutManager(this,space_50dp,space_20dp,line);
    }

    private BaseLine getLine() {
        return new SmoothLine();
    }

    // 通过构造好的Mind节点生成树，该步骤应在请求获得数据解析成对象后
    private void updateTreeViewNode(TreeModel<Mind> treeModel, NodeModel<Mind> parent) {
        List<NodeModel<Mind>> subList = new ArrayList<>();
        Mind root = parent.getValue();
        for (int i = 0; i < root.getSub().size(); i++) {
            NodeModel<Mind> node = new NodeModel<>(root.getSub().get(i));
            updateTreeViewNode(treeModel, node);
            subList.add(node);
        }
        NodeModel[] sub = subList.toArray(new NodeModel[0]);
        treeModel.addNode(parent, sub);
    }

    // 获取节点的subMind列表
    private List<Mind> getSubMinds(NodeModel<Mind> node) {
        LinkedList<NodeModel<Mind>> childNodes = node.getChildNodes();
        List<Mind> minds = new ArrayList<>();
        for (NodeModel<Mind> child : childNodes) {
            Mind subMind = child.getValue();
            subMind.setSub(getSubMinds(child));
            minds.add(subMind);
        }
        return minds;
    }

    // 获取mind的json结构字符串
    private String getMindStructure(TreeModel<Mind> treeModel) {
        NodeModel<Mind> root = treeModel.getRootNode();
        Mind mind = root.value;
        mind.setSub(getSubMinds(root));
        return gson.toJson(mind);
    }

    public void updateTreeViewNode(TreeModel<Mind> treeModel, String mindStr) {
        Type type = new TypeToken<ServerMind>() {}.getType();
        ServerMind serverMind = gson.fromJson(mindStr, type);

        NodeModel<Mind> done = new NodeModel<>(new Mind("已完成"));
        NodeModel<Mind> doing = new NodeModel<>(new Mind("正在做"));
        NodeModel<Mind> planing = new NodeModel<>(new Mind("计划中"));
        treeModel.addNode(treeModel.getRootNode(), done, doing, planing);

        for (String sub : serverMind.getDone()) {
            NodeModel<Mind> node = new NodeModel<>(new Mind(sub));
            treeModel.addNode(done, node);
        }

        for (String sub : serverMind.getDoing()) {
            NodeModel<Mind> node = new NodeModel<>(new Mind(sub));
            treeModel.addNode(doing, node);
        }

        for (String sub: serverMind.getPlaning()) {
            NodeModel<Mind> node = new NodeModel<>(new Mind(sub));
            treeModel.addNode(planing, node);
        }
    }

    private void setData(TreeViewAdapter<Mind> adapter){
        api.getWeekRecordSummarize(startTime, endTime, uuid).enqueue(new Callback<Result<WeekRecord>>() {
            @Override
            public void onResponse(Call<Result<WeekRecord>> call, Response<Result<WeekRecord>> response) {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (Exception ignored) {
                }
                WeekRecord weekRecord = response.body().getData();
                String mind = weekRecord.getMind();
                wrid = weekRecord.getWrid();
                Mind rootMind = gson.fromJson(mind, new TypeToken<Mind>() {}.getType());
                TreeModel<Mind> treeModel;
                // 尝试以json结构解析
                if (rootMind.getContent() == null) {
                    // 解析失败，说明是二级结构，以二级结构解析
                    treeModel = new TreeModel<>(new NodeModel<>(new Mind("周报总结")));
                    updateTreeViewNode(treeModel, mind);

                    // 解析后更新服务端mind数据
                    if (wrid != null) {
                        api.modifyMind(wrid, uuid, getMindStructure(treeModel)).enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable throwable) {

                            }
                        });
                    }
                } else {
                    NodeModel<Mind> root = new NodeModel<>(rootMind);
                    treeModel = new TreeModel<>(root);
                    updateTreeViewNode(treeModel, root);
                }

                // 将数据更新到UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setTreeModel(treeModel);
//                        binding.tvWrContent.setText(getChildrenContent(treeModel.getRootNode(), 0));
                        binding.tvWrContent.setText(weekRecord.getContent());
                    }
                });
            }

            @Override
            public void onFailure(Call<Result<WeekRecord>> call, Throwable throwable) {

            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void switchView(MindTreeViewAdapter adapter) {
        showMind = !showMind;
        if (showMind) {
            binding.rlMindContainer.setVisibility(View.VISIBLE);
            binding.llWrContainer.setVisibility(View.INVISIBLE);
        } else {
            binding.rlMindContainer.setVisibility(View.INVISIBLE);
            binding.llWrContainer.setVisibility(View.VISIBLE);
        }
//        binding.tvWrContent.setText(getChildrenContent(adapter.getTreeModel().getRootNode(), 0));
        adapter.notifyDataSetChange();
    }

    public String getChildrenContent(NodeModel<Mind> node, int depth) {
        if (depth > 2) {
            Log.i(TAG, "getChildrenContent: " + node.getValue().getContent());
            return "\t\t\t\t\t\t...\n";
        }
        LinkedList<NodeModel<Mind>> childNodes = node.getChildNodes();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < childNodes.size(); i++) {
            StringBuilder childNodeContent = new StringBuilder();
            StringBuilder nodeContent = new StringBuilder();
            NodeModel<Mind> childNode = childNodes.get(i);
            childNodeContent.append(getChildrenContent(childNode, depth + 1));
            for (int j = 0; j < depth; j++) {
                nodeContent.append("\t\t");
            }
            nodeContent.append(number2Title(i + 1, depth) == null ? "..." : number2Title(i + 1, depth));
            nodeContent.append(childNode.getValue().getContent()).append("\n");
            nodeContent.append(childNodeContent);
            result.append(nodeContent);
        }
        return result.toString();
    }

    public String number2Title1(int number) {
        String[] titleLevel1 = new String[] {"一、", "二、", "三、", "四、", "五、", "六、", "七、", "八、", "九、", "十、"};
        if (number > 10 || number <= 0) {
            return null;
        } else {
            return titleLevel1[number - 1];
        }
    }

    public String number2Title2(int number) {
        return number + ". ";
    }

    public String number2Title3(int number) {
        String[] titleLevel3 = new String[] {"a. ", "b. ", "c. ", "d. ", "e. ", "f. ", "g. ", "h. ", "i. ", "j. ", "k. ", "l. ", "m. ", "n. ", "o. ", "p. ", "q. ", "r. ", "s. ", "t. ", "u. ", "v. ", "w. ", "x. ", "y. ", "z. "};
        if (number <= 0 || number > 26) {
            return null;
        } else {
            return titleLevel3[number - 1];
        }
    }

    public String number2Title(int number, int depth) {
        if (depth > 2 || depth < 0) {
            return null;
        }
        if (depth == 0) {
            return number2Title1(number);
        } else if (depth == 1) {
            return number2Title2(number);
        } else {
            return number2Title3(number);
        }
    }
}