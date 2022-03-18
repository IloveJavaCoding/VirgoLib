package com.nepalese.virgolib.widget.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;

import com.nepalese.virgolib.bean.AudioBean;
import com.nepalese.virgosdk.Util.MathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2022/3/18.
 * Usage: virgo music player
 */
public class VMPlayer implements MediaPlayer.OnCompletionListener, VirgoPlayerCallback {
    private static final long INTERVAL_GET_PROGRESS = 500;//后台获取进度频率

    //播放器状态
    public static final int STATE_ERROR = -1; //错误状态：需要重置列表才能继续使用
    public static final int STATE_INITIAL = 0;//初始化状态
    public static final int STATE_PREPARED = 1;//播放列表/资源已设置
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSE = 3;

    //播放模式
    public static final int MODE_SINGLE = 0;//单曲循环
    public static final int MODE_LOOP = 1;//列表循环
    public static final int MODE_RANDOM = 2;//列表随机

    private static volatile VMPlayer instance;//单例
    private MediaPlayer mediaPlayer;
    private List<AudioBean> beanList;//当前播放列表
    private List<iPlayBack> iPlayBacks;//已注册回调列表
    private AudioBean curBean;//当前在播放的音频

    private int curState;//当前播放状态
    private int curIndex;//当前播放索引
    private int curMode;//当前播放模式
    private int errTime;//播放器连续出错次数
    private int aimSeek;//播放前设置的进度

    public static VMPlayer getInstance() {
        if (instance == null) {
            synchronized (VMPlayer.class) {
                if (instance == null) {
                    instance = new VMPlayer();
                }
            }
        }
        return instance;
    }

    private VMPlayer() {
        beanList = new ArrayList<>();
        iPlayBacks = new ArrayList<>(5);//最多同时存在回调个数

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(this);

        curState = STATE_INITIAL;
        curMode = MODE_LOOP;//默认
        curIndex = 0;
        errTime = 0;
        aimSeek = 0;
    }

    /**
     * 播放器是否可播放
     * @return
     */
    private boolean isValid() {
        return curState >= STATE_PREPARED && !beanList.isEmpty();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(curMode==MODE_SINGLE){
            //单曲循环时自动重复播放
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }else{
            notifyComplete();
        }
    }

    /**
     * 播放|继续播放
     */
    @Override
    public void play() {
        if (isValid()) {
            if (curState == STATE_PAUSE) {
                //继续播放
                curState = STATE_PLAYING;
                mediaPlayer.start();
                notifyStateChanged(true);
            } else if (curState == STATE_PREPARED) {
                prepareAndPlay();
            }
            //正在播放...
        } else {
            notifyError("未设置播放列表！");
        }
    }

    private void prepareAndPlay(){
        curState = STATE_PREPARED;
        if (curIndex < 0 || curIndex >= beanList.size()) {
            curIndex = 0;
        }
        playResource(beanList.get(curIndex));
    }

    private void playResource(AudioBean bean) {
        startTask();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(bean.getPath());//本地文件、在线链接
            mediaPlayer.setOnPreparedListener(mp -> {
                notifySongChanged(bean);
                notifyStateChanged(true);
                curState = STATE_PLAYING;
                mediaPlayer.seekTo(aimSeek);
                mediaPlayer.start();
                errTime = 0;
                aimSeek = 0;
                curBean = bean;
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            ++errTime;
            if (errTime >= beanList.size()) {
                //需要重置列表才能继续使用
                curState = STATE_ERROR;
            } else {
                //重置状态
                if (beanList.size() > 0) {
                    curState = STATE_PREPARED;
                } else {
                    curState = STATE_INITIAL;
                }
            }
            notifyStateChanged(false);
            notifyError("播放器出错！" + e.getMessage());
        }
    }

    /**
     * 播放当前列表指定位置
     * @param index
     */
    @Override
    public void play(int index) {
        if (isValid()) {
            curIndex = index;
            prepareAndPlay();
        }else {
            notifyError("未设置播放列表！");
        }
    }

    /**
     * 临时播放某个音频文件
     * @param bean AudioBean
     */
    @Override
    public void play(AudioBean bean) {
        if (bean == null) {
            notifyError("指定歌曲为空！");
            return;
        }

        curState = STATE_PREPARED;
        playResource(bean);
    }

    /**
     * 更换播放列表
     * @param list 新列表
     * @param index 开始位置，默认：0
     */
    @Override
    public void play(List<AudioBean> list, int index) {
        if (list == null || list.isEmpty()) {
            notifyError("新列表为空！");
            return;
        }

        curIndex = index;
        setPlayList(list);
        prepareAndPlay();
    }

    /**
     * 上一首
     */
    @Override
    public void playLast() {
        if(isValid()){
            switch (curMode) {
                case MODE_SINGLE:
                    break;
                case MODE_LOOP:
                    if (curIndex>0){
                        --curIndex;
                    }else{
                        curIndex = beanList.size()-1;
                    }
                    prepareAndPlay();
                    break;
                case MODE_RANDOM:
                    curIndex = MathUtil.getRandom(0, beanList.size(), curIndex);
                    prepareAndPlay();
                    break;
            }
        }else {
            notifyError("未设置播放列表！");
        }
    }

    /**
     * 下一首
     */
    @Override
    public void playNext() {
        if(isValid()){
            switch (curMode) {
                case MODE_SINGLE:
                    break;
                case MODE_LOOP:
                    ++curIndex;
                    prepareAndPlay();
                    break;
                case MODE_RANDOM:
                    curIndex = MathUtil.getRandom(0, beanList.size(), curIndex);
                    prepareAndPlay();
                    break;
            }
        }else {
            notifyError("未设置播放列表！");
        }
    }

    /**
     * 暂停播放
     */
    @Override
    public void pause() {
        if (isPlaying()) {
            curState = STATE_PAUSE;
            mediaPlayer.pause();
            notifyStateChanged(false);
        }
    }

    /**
     * 跳转播放进度
     * @param progress
     */
    @Override
    public void seekTo(int progress) {
        if(isValid()){
            if(curState>STATE_PREPARED){
                aimSeek = 0;
                mediaPlayer.seekTo(progress);
            }else{
                aimSeek = progress;
            }
        }
    }

    /**
     * 设置播放列表
     * @param beans
     */
    @Override
    public void setPlayList(List<AudioBean> beans) {
        if(beans==null || beans.isEmpty()){
            notifyError("新列表为空！");
            return;
        }

        curState = STATE_PREPARED;
        beanList.clear();
        beanList.addAll(beans);
        curBean = beanList.get(curIndex);
    }

    /**
     * 设置播放模式
     * @param mode
     */
    @Override
    public void setPlayMode(int mode) {
        this.curMode = mode;
    }

    /**
     * 是否正在播放
     * @return
     */
    @Override
    public boolean isPlaying() {
        return isValid() && mediaPlayer.isPlaying();
    }

    /**
     * 当前播放进度
     * @return
     */
    @Override
    public int getCurProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 当前播放器状态
     * @return
     */
    @Override
    public int getCurState() {
        return curState;
    }

    /**
     * 获取当前播放音频信息
     * @return
     */
    @Override
    public AudioBean getCurMusic() {
        if(isValid()){
            return curBean;
        }
        return null;
    }

    /**
     * 注销播放器
     */
    @Override
    public void releasePlayer() {
        stopTask();
        if(iPlayBacks!=null){
            iPlayBacks.clear();
            iPlayBacks = null;
        }
        if(beanList!=null){
            beanList.clear();
            beanList = null;
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        instance = null;
        curState = STATE_INITIAL;
    }

    /**
     * 注册播放器回调
     * @param callback
     */
    @Override
    public void registerCallback(iPlayBack callback) {
        iPlayBacks.add(callback);
    }

    /**
     * 注销播放器回调
     * @param callback
     */
    @Override
    public void unregisterCallback(iPlayBack callback) {
        iPlayBacks.remove(callback);
    }

    @Override
    public void removeCallbacks() {
        iPlayBacks.clear();
    }

    private void notifySongChanged(AudioBean bean) {
        for (iPlayBack callBack : iPlayBacks) {
            callBack.onChangeSong(bean);
        }
    }

    private void notifyStateChanged(boolean isPlaying) {
        for (iPlayBack callback : iPlayBacks) {
            callback.onPlayStateChanged(isPlaying);
        }
    }

    private void notifyComplete() {
        for (iPlayBack callback : iPlayBacks) {
            callback.onPlayCompleted();
        }
    }

    private void notifyProcessChanged(int process) {
        for (iPlayBack callback : iPlayBacks) {
            callback.onProcessChanged(process);
        }
    }

    private void notifyError(String msg) {
        for (iPlayBack callback : iPlayBacks) {
            callback.onPlayError(curState, msg);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private final Handler handler = new Handler(msg -> false);

    private final Runnable getProcessTask = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(getProcessTask, INTERVAL_GET_PROGRESS);
            try {
                if (isPlaying()) {
                    notifyProcessChanged(getCurProgress());
                }
            } catch (Throwable ignored) {}
        }
    };

    private void startTask() {
        stopTask();
        handler.post(getProcessTask);
    }

    private void stopTask() {
        handler.removeCallbacks(getProcessTask);
    }
}
