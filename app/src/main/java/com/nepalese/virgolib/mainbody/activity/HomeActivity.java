package com.nepalese.virgolib.mainbody.activity;

import android.os.Bundle;
import android.widget.GridView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.base.BaseActivity;
import com.nepalese.virgolib.bean.TaskBean;
import com.nepalese.virgolib.mainbody.Adapter.Adapter_GridView_Task;
import com.nepalese.virgolib.mainbody.activity.database.DatabaseActivity;
import com.nepalese.virgolib.mainbody.activity.download.DownloadActivity;
import com.nepalese.virgolib.mainbody.activity.fileopt.FileOperationActivity;
import com.nepalese.virgolib.mainbody.activity.imageopt.ImageOperationActivity;
import com.nepalese.virgolib.mainbody.activity.network.NetworkActivity;
import com.nepalese.virgolib.mainbody.activity.oricom.OriComponentActivity;
import com.nepalese.virgolib.mainbody.activity.selfcom.SelfComponentActivity;
import com.nepalese.virgolib.mainbody.activity.thirdlib.ThirdLibActivity;
import com.nepalese.virgosdk.Util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    private GridView gridView;
    private List<TaskBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void initUI() {
        gridView = findViewById(R.id.gridViewHome);
    }

    @Override
    protected void initData() {
        createTasks();
        gridView.setAdapter(new Adapter_GridView_Task(getApplicationContext(), list));
    }

    private void createTasks() {
        list = new ArrayList<>();

        TaskBean bean1 = new TaskBean("原装控件", OriComponentActivity.class);
        list.add(bean1);
        TaskBean bean2 = new TaskBean("第三方组件", ThirdLibActivity.class);
        list.add(bean2);
        TaskBean bean3 = new TaskBean("自定义控件", SelfComponentActivity.class);
        list.add(bean3);
        TaskBean bean4 = new TaskBean("图片操控", ImageOperationActivity.class);
        list.add(bean4);
        TaskBean bean5 = new TaskBean("网络连接", NetworkActivity.class);
        list.add(bean5);
        TaskBean bean6 = new TaskBean("数据库", DatabaseActivity.class);
        list.add(bean6);
        TaskBean bean7 = new TaskBean("下载模块", DownloadActivity.class);
        list.add(bean7);
        TaskBean bean8 = new TaskBean("文件读写", FileOperationActivity.class);
        list.add(bean8);
    }

    @Override
    protected void setListener() {
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            SystemUtil.jumActivity(this, list.get(position).getC());
        });
    }
}