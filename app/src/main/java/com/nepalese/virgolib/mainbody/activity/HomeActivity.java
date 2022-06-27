package com.nepalese.virgolib.mainbody.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.GridView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.base.MyApp;
import com.nepalese.virgolib.bean.TaskBean;
import com.nepalese.virgolib.helper.CommonHelper;
import com.nepalese.virgolib.mainbody.Adapter.Adapter_GridView_Task;
import com.nepalese.virgolib.mainbody.activity.database.DatabaseActivity;
import com.nepalese.virgolib.mainbody.activity.demo.DemoActivity;
import com.nepalese.virgolib.mainbody.activity.download.DownloadActivity;
import com.nepalese.virgolib.mainbody.activity.fileopt.FileOperationActivity;
import com.nepalese.virgolib.mainbody.activity.game.GameActivity;
import com.nepalese.virgolib.mainbody.activity.math.MathActivity;
import com.nepalese.virgolib.mainbody.activity.mediaopt.MediaOptActivity;
import com.nepalese.virgolib.mainbody.activity.network.NetworkActivity;
import com.nepalese.virgolib.mainbody.activity.oricom.OriComponentActivity;
import com.nepalese.virgolib.mainbody.activity.selfcom.SelfComponentActivity;
import com.nepalese.virgolib.mainbody.activity.thirdlib.ThirdLibActivity;
import com.nepalese.virgolib.widget.musicplayer.VirgoSimplePlayer;
import com.nepalese.virgosdk.Base.BaseActivity;
import com.nepalese.virgosdk.Util.SystemUtil;
import com.nepalese.virgosdk.Util.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    private GridView gridView;
    private List<TaskBean> list;
    private VirgoSimplePlayer simplePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UIUtil.setStatusTrans(this);
        }
        setContentView(R.layout.activity_home);
        init();
    }

    @Override
    protected void initUI() {
        gridView = findViewById(R.id.gridview_home);
        simplePlayer = findViewById(R.id.simplePlayer);
    }

    @Override
    protected void initData() {
        createTasks();
        if(MyApp.getInstance().isLandscape()){
            gridView.setNumColumns(8);
            gridView.setColumnWidth((int) getResources().getDimension(R.dimen.home_grid_block_land));
        }else{
            gridView.setNumColumns(4);
            gridView.setColumnWidth((int) getResources().getDimension(R.dimen.home_grid_block_portrait));
        }

        gridView.setAdapter(new Adapter_GridView_Task(getApplicationContext(), list, MyApp.getInstance().isLandscape()));

        simplePlayer.setPlayList(CommonHelper.getAudioItems(this));
    }

    private void createTasks() {
        list = new ArrayList<>();

        TaskBean bean = new TaskBean("原装控件", OriComponentActivity.class);
        list.add(bean);
        bean = new TaskBean("第三方组件", ThirdLibActivity.class);
        list.add(bean);
        bean = new TaskBean("自定义控件", SelfComponentActivity.class);
        list.add(bean);
        bean = new TaskBean("图片操控", MediaOptActivity.class);
        list.add(bean);
        bean = new TaskBean("网络连接", NetworkActivity.class);
        list.add(bean);
        bean = new TaskBean("数据库", DatabaseActivity.class);
        list.add(bean);
        bean = new TaskBean("下载模块", DownloadActivity.class);
        list.add(bean);
        bean = new TaskBean("文件读写", FileOperationActivity.class);
        list.add(bean);
        bean = new TaskBean("案例解析", DemoActivity.class);
        list.add(bean);
        bean = new TaskBean("游戏城", GameActivity.class);
        list.add(bean);
        bean = new TaskBean("计算工具", MathActivity.class);
        list.add(bean);
    }

    @Override
    protected void setListener() {
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            SystemUtil.jumActivity(this, list.get(position).getC());
        });
    }

    @Override
    protected void release() {
        list.clear();
        simplePlayer.releasePlayer();
    }

    @Override
    protected void onBack() {
        //
    }
}