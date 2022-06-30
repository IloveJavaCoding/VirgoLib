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
public class LyricAnimActivity extends BaseActivity implements
        VirgoFileSelectorDialog.SelectFileCallback, BaseLrcView.LrcCallback {
    private static final String TAG = "LyricAnimActivity";

    private BaseLrcView lrcView;
    private VirgoFileSelectorDialog fileSelectorDialog;
    private long curTime = 0;

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
            lrcView.setTextSize(22f);
            lrcView.setDividerHeight(18f);
            fileSelectorDialog.setDialogWidth(MyApp.getInstance().getsWidth() / 2);
        } else {
            fileSelectorDialog.setDialogWidth(MyApp.getInstance().getsWidth() * 2 / 3);
        }
        fileSelectorDialog.setDialogHeight(MyApp.getInstance().getsHeight() / 2);
    }

    @Override
    protected void setListener() {
        fileSelectorDialog.setCallback(this);
        lrcView.setCallback(this);
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

    @Override
    public void onUpdateTime(long time) {
        handler.removeCallbacks(timeTisk);
        curTime = time;
        lrcView.seekTo(time);
        handler.postDelayed(timeTisk, INTERVAL_FLASH);
    }

    @Override
    public void onFinish() {
        stopTask();
    }

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
            curTime += INTERVAL_FLASH;
            lrcView.seekTo(curTime);
            handler.postDelayed(timeTisk, INTERVAL_FLASH);
        }
    };

    private void startTask() {
        stopTask();
        handler.post(timeTisk);
    }

    private void stopTask() {
        handler.removeCallbacks(timeTisk);
    }

    private final long INTERVAL_FLASH = 200L;
    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
}