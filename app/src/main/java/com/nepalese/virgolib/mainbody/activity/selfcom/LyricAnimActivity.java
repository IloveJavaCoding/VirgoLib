package com.nepalese.virgolib.mainbody.activity.selfcom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.nepalese.virgocomponent.view.VirgoFileSelectorDialog;
import com.nepalese.virgolib.R;
import com.nepalese.virgolib.config.MyApp;
import com.nepalese.virgolib.widget.lrc.BaseLrcView;
import com.nepalese.virgosdk.Base.BaseActivity;
import com.nepalese.virgosdk.Util.FileUtil;
import com.nepalese.virgosdk.Util.UIUtil;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;

//全屏模式, 常亮
public class LyricAnimActivity extends BaseActivity implements VirgoFileSelectorDialog.SelectFileCallback {
    private static final String TAG = "LyricAnimActivity";

//    private VirgoLrcView lrcView;
    private BaseLrcView lrcView;
    private VirgoFileSelectorDialog fileSelectorDialog;
    private int curTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_anim);
        //隐藏顶部状态栏
        UIUtil.setSNHide(this);

        init();
    }

    @Override
    protected void initUI() {
        lrcView = findViewById(R.id.lrcView);
//        lrcView.setCallback(this);
    }

    @Override
    protected void initData() {
        lrcView.setLrc(FileUtil.readTxtResource(getApplicationContext(), R.raw.shaonian, "utf-8"));
        lrcView.seekTo(0);

        fileSelectorDialog = new VirgoFileSelectorDialog(this);
        fileSelectorDialog.setFlag(VirgoFileSelectorDialog.FLAG_FILE);
        fileSelectorDialog.setFileType(VirgoFileSelectorDialog.TYPE_ONLY_ONE);
        fileSelectorDialog.setUniqueSuffix("lrc");

        if (MyApp.getInstance().isLandscape()) {
            fileSelectorDialog.setDialogWidth(MyApp.getInstance().getsWidth() / 2);
        } else {
            fileSelectorDialog.setDialogWidth(MyApp.getInstance().getsWidth() * 2 / 3);
        }
        fileSelectorDialog.setDialogHeight(MyApp.getInstance().getsHeight() / 2);
    }

    @Override
    protected void setListener() {
        fileSelectorDialog.setCallback(this);
    }

    @Override
    protected void release() {
        stopTask();
        if (fileSelectorDialog != null) {
            fileSelectorDialog.destory();
        }
    }

    @Override
    protected void onBack() {
        finish();
    }

//    @Override
//    public void onRefresh(long time) {
//        curTime = (int) (time / 1000);
//        lrcView.seekTo(time);
//    }

    @Override
    public void onResult(List<File> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        stopTask();
        lrcView.setLrcFile(list.get(0).getPath());
        lrcView.seekTo(0);
    }

    public void onSelectFile(View view) {
        if (fileSelectorDialog != null) {
            fileSelectorDialog.show();
        }
    }

    public void onStartPlay(View view) {
        startTask();
    }

    public void onStopPlay(View view) {
        stopTask();
    }

    private final Runnable timeTisk = new Runnable() {
        @Override
        public void run() {
            ++curTime;
            lrcView.seekTo(curTime * 1000);
            handler.postDelayed(timeTisk, 1000);
        }
    };

    private void startTask() {
        stopTask();
        handler.post(timeTisk);
//        lrcView.setPlaying(true);
    }

    private void stopTask() {
        handler.removeCallbacks(timeTisk);
//        lrcView.setPlaying(false);
    }

    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
}