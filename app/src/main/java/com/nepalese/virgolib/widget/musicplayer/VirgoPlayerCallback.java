package com.nepalese.virgolib.widget.musicplayer;

import com.nepalese.virgolib.bean.AudioBean;
import java.util.List;
import androidx.annotation.NonNull;

/**
 * Created by Administrator on 2022/3/18.
 * Usage: 音乐播放器公开接口
 */
public interface VirgoPlayerCallback {
    //播放|继续播放
    void play();

    //播放当前列表指定位置
    void play(int index);

    //临时播放某个音频文件
    void play(AudioBean bean);

    //更换播放列表
    void play(List<AudioBean> beanList, int index);

    //上一首
    void playLast();

    //下一首
    void playNext();

    //暂停播放
    void pause();

    //跳转播放进度
    void seekTo(int progress);

    //设置播放列表
    void setPlayList(List<AudioBean> beans);

    //设置播放模式
    void setPlayMode(int mode);

    //是否正在播放
    boolean isPlaying();

    //当前播放进度
    int getCurProgress();

    //当前播放器状态
    int getCurState();

    //获取当前播放音频信息
    AudioBean getCurMusic();

    //注销播放器
    void releasePlayer();

    void registerCallback(iPlayBack callback);

    void unregisterCallback(iPlayBack callback);

    void removeCallbacks();

    interface iPlayBack {
        //切换音频后调用
        void onChangeSong(@NonNull AudioBean bean);

        //播放结束时调用
        void onPlayCompleted();

        //播放状态变化时调用：播放|暂停
        void onPlayStateChanged(boolean isPlaying);

        //播放进度变化时调用
        void onProcessChanged(int process);

        //播放出错时调用
        void onPlayError(int state, String error);
    }
}
