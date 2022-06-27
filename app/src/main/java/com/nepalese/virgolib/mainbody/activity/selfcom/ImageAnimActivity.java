package com.nepalese.virgolib.mainbody.activity.selfcom;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.nepalese.virgocomponent.view.VirgoFileSelectorDialog;
import com.nepalese.virgolib.R;
import com.nepalese.virgolib.base.MyApp;
import com.nepalese.virgolib.widget.image.BaseImageView;
import com.nepalese.virgosdk.Base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAnimActivity extends BaseActivity implements VirgoFileSelectorDialog.SelectFileCallback{

    private VirgoFileSelectorDialog fileSelectorDialog;
    private BaseImageView baseImageView;
    private List<String> imgList;//图片文件路径
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_anim);
        init();
        scanFiles("");
    }

    @Override
    protected void initUI() {
        baseImageView = findViewById(R.id.base_image);
    }

    @Override
    protected void initData() {
        imgList = new ArrayList<>();
        baseImageView.setAnimType(BaseImageView.ANIM_RANDOM);

        fileSelectorDialog = new VirgoFileSelectorDialog(this);
        fileSelectorDialog.setFlag(VirgoFileSelectorDialog.FLAG_DIR);
        fileSelectorDialog.setDialogHeight(MyApp.getInstance().getsHeight() / 2);
    }

    @Override
    protected void setListener() {
        fileSelectorDialog.setCallback(this);
    }

    @Override
    protected void release() {
        stopPlay();
        if (baseImageView != null) {
            baseImageView.releaseBase();
        }
        if (fileSelectorDialog != null) {
            fileSelectorDialog.destory();
        }
    }

    @Override
    protected void onBack() {
        finish();
    }

    @Override
    public void onResult(List<File> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        scanFiles(list.get(0).getPath());
    }

    private void startPlay() {
        handler.sendEmptyMessage(MSG_CHANGEIMG);
    }

    private void stopPlay() {
        handler.removeMessages(MSG_CHANGEIMG);
    }

    public void onSelectDir(View view) {
        //切换根目录
        if (fileSelectorDialog != null) {
            fileSelectorDialog.show();
        }
    }

    private void scanFiles(String dir) {
        if (TextUtils.isEmpty(dir)) {
            dir = Environment.getExternalStorageDirectory().getPath() + "/Pictures";
        }

        File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            String[] names = file.list();
            if (names == null || names.length < 1) {
                showToast("空文件夹！");
                return;
            }

            stopPlay();
            imgList.clear();
            if (names != null) {
                for (String name : names) {
                    if (name.endsWith("jpg") || name.endsWith("png")) {
                        imgList.add(dir + File.separator + name);
                    }
                }
            }

            if(imgList.size()>0){
                startPlay();
            }else{
                showToast("未找到图片！");
            }
        } else {
            showToast("路径不存在！");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private final int MSG_CHANGEIMG = 1;
    private final long DELAY_CHANGE = 30000L;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_CHANGEIMG) {
                changeImageRes();
                handler.sendEmptyMessageDelayed(MSG_CHANGEIMG, DELAY_CHANGE);
            }
            return false;
        }
    });

    private void changeImageRes() {
        if (baseImageView != null) {
            if (index >= imgList.size()) {
                index = 0;
            }

            baseImageView.setImageResource(imgList.get(index));
            index++;
        }
    }
}