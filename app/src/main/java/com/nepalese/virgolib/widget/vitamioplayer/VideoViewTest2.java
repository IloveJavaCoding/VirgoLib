package com.nepalese.virgolib.widget.vitamioplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2022/3/26.
 * Usage:
 */

public class VideoViewTest2 extends VideoView {
    private static final String TAG = "VideoViewTest";

    private int mCurrentIndex;
    private List<String> mUrls;

    public VideoViewTest2(Context context) {
        this(context, null);
    }

    public VideoViewTest2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoViewTest2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mCurrentIndex = 0;
        this.mUrls = new ArrayList<>();
        this.requestFocus();

        this.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                load();
            }
        });

    }

    public void playNext(){
        if (mUrls == null || mUrls.isEmpty()) {
            return;
        }
        load();
    }

    public void setUrl(List<String> urls) {
        if (urls != null && !urls.isEmpty()) {
            mUrls.clear();
            mUrls.addAll(urls);
        }
    }

    public void play() {
        if (mUrls == null || mUrls.isEmpty()) {
            return;
        }
        load();
    }

    private void load() {
        if (mUrls == null || mUrls.isEmpty()) return;//防止在获取surface之前进入，导致+1
        Log.i(TAG,  " current index: "+ mCurrentIndex + ", list size:" + mUrls.size());

        if (mCurrentIndex >= mUrls.size()) {
            mCurrentIndex = 0;
        }

        String path = mUrls.get(mCurrentIndex);
        mCurrentIndex++;
        if(path.contains("http")){
            Log.i(TAG, "load: 在线播放！");
            this.setVideoPath(path);
            this.start();
        }else{
            File file = new File(path);
            if (file.exists()) {
                Log.i(TAG, "play: " + path);
                //播放本地视频
                this.setVideoURI(Uri.fromFile(file));
                this.start();
            }else{
                Log.e(TAG, "load: 文件不存在");
                load();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        release();
        super.onDetachedFromWindow();
    }

    public void release() {
        this.stopPlayback();
        this.clearFocus();
    }
}
