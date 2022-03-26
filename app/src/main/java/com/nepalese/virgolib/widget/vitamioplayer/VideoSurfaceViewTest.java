package com.nepalese.virgolib.widget.vitamioplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Administrator on 2021/6/3.
 * Usage:
 */

public class VideoSurfaceViewTest extends BaseSurfaceVideoPlayer {
    private static final String TAG = "VideoSurfaceView";

    private int mCurrentIndex;
    private List<String> mUrls;

    public VideoSurfaceViewTest(Context context) {
        this(context, null);
    }

    public VideoSurfaceViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSurfaceViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mCurrentIndex = 0;
        this.mUrls = new ArrayList<>();

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
                this.setVideoPath(path);
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
        this.releasePlayer();
        this.clearFocus();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            this.pause();
        } else {
            this.start();
        }
    }
}
