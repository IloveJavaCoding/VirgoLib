package com.nepalese.virgolib.mainbody.activity.thirdlib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.widget.vitamioplayer.VideoViewTest2;
import com.nepalese.virgosdk.Base.BaseActivity;
import com.nepalese.virgosdk.Util.WindowUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayTestActivity extends BaseActivity {
    private static final String TAG = "VideoPlayTestActivity";

    private RelativeLayout root;
    //    private VideoSurfaceViewTest videoView;
//    private VideoViewTest videoView;
    private VideoViewTest2 videoView;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_test);
    }
    @Override
    protected void initUI() {
        root = findViewById(R.id.layoutTestRoot);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
//        list.add("http://192.168.2.19:9991/cdmsa/2022/03/25/e21e8dabca1c2ec0.mp4");
        String path = Environment.getExternalStorageDirectory().getPath() + "/harine/bus";///harine/bus /Movies
        File file = new File(path);
        if (file.exists()) {
            File[] filePath = file.listFiles();
            if (filePath != null) {
//                Arrays.sort(filePath, new Comparator<File>() {
//                    @Override
//                    public int compare(File o1, File o2) {
//                        return o1.getName().compareTo(o2.getName());
//                    }
//                });

                for (File p : filePath) {
                    Log.i(TAG, "add: " + p.getName());
                    if (p.getName().endsWith("mp4")) {
                        list.add(p.getAbsolutePath());
                    }
                }
            }
        }
    }

    @Override
    protected void setListener() {
        addVideo();
        startPlay();
    }

    @Override
    protected void release() {
        removeVideo();
    }

    @Override
    protected void onBack() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                startPlay();
                return true;
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
                controlPlay();
                return true;
            case KeyEvent.KEYCODE_3:
                playNext();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playNext() {
        if(videoView!=null){
            videoView.playNext();
        }
    }

    private void addVideo() {
        if (videoView == null) {
            Log.i(TAG, "addVideo: ");

//            videoView = new VideoSurfaceViewTest(this);
//            videoView = new VideoViewTest(this);
            videoView = new VideoViewTest2(this);
            root.addView(videoView);
            updateLayoutParams(videoView, 0, 0,
                    WindowUtil.getScreenWidth(this),
                    WindowUtil.getScreenHeight(this));
        } else {
            Log.e(TAG, "重复添加: ");
        }
    }

    private void removeVideo() {
        if (videoView != null) {
            Log.i(TAG, "removeVideo: ");
            videoView.release();
            videoView = null;
            root.removeAllViews();
        }
    }

    //设置控件位置
    private void updateLayoutParams(View view, int left, int top, int width, int height) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            lp.leftMargin = left;
            lp.topMargin = top;
            lp.width = width;
            lp.height = height;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (view != null) {
            view.setLayoutParams(lp);
        }
    }

    private void startPlay() {
        if (videoView != null) {
            Log.i(TAG, "startPlay: ");
            if (!list.isEmpty()) {
                videoView.setUrl(list);
                videoView.play();
            }
        }
    }

    private void controlPlay() {
        if (videoView != null) {
            if (videoView.isPlaying()) {
                Log.i(TAG, "pausePlay: ");
                videoView.pause();
            } else {
                videoView.start();
            }
        }
    }
}