package com.nepalese.virgolib.widget.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.bean.AudioItem;
import com.nepalese.virgosdk.Util.ConvertUtil;
import com.nepalese.virgosdk.Util.MathUtil;
import com.nepalese.virgosdk.Util.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2022/3/18.
 * Usage: 简单音乐播放器控件
 */

public class VirgoSimplePlayer extends RelativeLayout implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "VirgoSimplePlayer";

    private SeekBar musicSeekbar;
    private TextView musicName, musicCur, musicAll;
    private ImageButton musicLast, musicPlay, musicNext, musicMode;

    public VirgoSimplePlayer(Context context) {
        this(context, null);
    }

    public VirgoSimplePlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirgoSimplePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_simple_virgo_player, this, true);
        init();
    }

    private void init() {
        initUI();
        initData();
        setListener();
    }

    private void initUI() {
        musicSeekbar = findViewById(R.id.music_seekbar);
        musicName = findViewById(R.id.music_tv_name);
        musicCur = findViewById(R.id.music_cur);
        musicAll = findViewById(R.id.music_all);

        musicLast = findViewById(R.id.music_btn_last);
        musicPlay = findViewById(R.id.music_btn_paly);
        musicNext = findViewById(R.id.music_btn_next);
        musicMode = findViewById(R.id.music_btn_mode);
    }

    private void initData() {
        beanList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(this);

        curState = STATE_INITIAL;
        curMode = MODE_LOOP;//默认
        curIndex = 0;
        errTime = 0;
        aimSeek = 0;
    }

    private void setListener() {
        musicLast.setOnClickListener(v -> playLast());

        musicNext.setOnClickListener(v -> playNext());

        musicPlay.setOnClickListener(v -> playOrPause());

        musicMode.setOnClickListener(v -> changPlayMode());

        musicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
            }
        });
    }

    private void changPlayMode() {
        curMode++;
        if(curMode >= MODE_RANDOM){
            curMode = MODE_SINGLE;
        }
        switch (curMode){
            case MODE_SINGLE:
                musicMode.setImageResource(R.mipmap.icon_single);
                break;
            case MODE_LOOP:
                musicMode.setImageResource(R.mipmap.icon_order);
                break;
            case MODE_RANDOM:
                musicMode.setImageResource(R.mipmap.icon_random);
                break;
        }
    }

    private void playOrPause() {
        if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void notifyStateChanged(boolean isPlaying) {
        if (isPlaying) {
            musicPlay.setImageResource(R.mipmap.icon_play);
        } else {
            musicPlay.setImageResource(R.mipmap.icon_pause);
        }
    }

    private void notifyError(String s) {
        Log.e(TAG, s);
    }

    private void notifyProcessChanged(int process) {
        musicSeekbar.setProgress(process);
        musicCur.setText(ConvertUtil.formatTime(process));
    }

    private void notifySongChanged(String name, int duration) {
        musicName.setText(name);
        musicSeekbar.setMax(duration);
        musicAll.setText(ConvertUtil.formatTime(duration));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
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

    private MediaPlayer mediaPlayer;
    private List<AudioItem> beanList;//当前播放列表

    private int curState;//当前播放状态
    private int curIndex;//当前播放索引
    private int curMode;//当前播放模式
    private int errTime;//播放器连续出错次数
    private int aimSeek;//播放前设置的进度

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (curMode == MODE_SINGLE) {
            //单曲循环时自动重复播放
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            playNext();
        }
    }

    /**
     * 播放器是否可播放
     * @return
     */
    private boolean isValid() {
        return curState >= STATE_PREPARED && !beanList.isEmpty();
    }

    /**
     * 播放|继续播放
     */
    private void play() {
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

    private void prepareAndPlay() {
        curState = STATE_PREPARED;
        if (curIndex < 0 || curIndex >= beanList.size()) {
            curIndex = 0;
        }
        playResource(beanList.get(curIndex));
    }

    private void playResource(AudioItem bean) {
        startTask();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(bean.getPath());//本地文件、在线链接
            mediaPlayer.setOnPreparedListener(mp -> {
                notifySongChanged(bean.getName(), mediaPlayer.getDuration());
                notifyStateChanged(true);
                curState = STATE_PLAYING;
                mediaPlayer.seekTo(aimSeek);
                mediaPlayer.start();
                errTime = 0;
                aimSeek = 0;
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
     *
     * @param index
     */
    public void play(int index) {
        if (isValid()) {
            curIndex = index;
            prepareAndPlay();
        } else {
            notifyError("未设置播放列表！");
        }
    }

    /**
     * 临时播放某个音频文件
     *
     * @param bean AudioItem
     */
    public void play(AudioItem bean) {
        if (bean == null) {
            notifyError("指定歌曲为空！");
            return;
        }

        curState = STATE_PREPARED;
        playResource(bean);
    }

    /**
     * 更换播放列表
     *
     * @param list  新列表
     * @param index 开始位置，默认：0
     */
    public void play(List<AudioItem> list, int index) {
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
    private void playLast() {
        if (isValid()) {
            switch (curMode) {
                case MODE_SINGLE:
                    break;
                case MODE_LOOP:
                    if (curIndex > 0) {
                        --curIndex;
                    } else {
                        curIndex = beanList.size() - 1;
                    }
                    prepareAndPlay();
                    break;
                case MODE_RANDOM:
                    curIndex = MathUtil.getRandom(0, beanList.size(), curIndex);
                    prepareAndPlay();
                    break;
            }
        } else {
            notifyError("未设置播放列表！");
        }
    }

    /**
     * 下一首
     */
    private void playNext() {
        if (isValid()) {
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
        } else {
            notifyError("未设置播放列表！");
        }
    }

    /**
     * 暂停播放
     */
    private void pause() {
        if (isPlaying()) {
            curState = STATE_PAUSE;
            mediaPlayer.pause();
            notifyStateChanged(false);
        }
    }

    /**
     * 跳转播放进度
     *
     * @param progress
     */
    private void seekTo(int progress) {
        if (isValid()) {
            if (curState > STATE_PREPARED) {
                aimSeek = 0;
                mediaPlayer.seekTo(progress);
            } else {
                aimSeek = progress;
            }
        }
    }

    /**
     * 设置播放列表
     *
     * @param beans
     */
    public void setPlayList(List<AudioItem> beans) {
        if (beans == null || beans.isEmpty()) {
            notifyError("新列表为空！");
            return;
        }

        curState = STATE_PREPARED;
        beanList.clear();
        beanList.addAll(beans);
    }

    /**
     * 设置播放模式
     *
     * @param mode
     */
    private void setPlayMode(int mode) {
        this.curMode = mode;
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    private boolean isPlaying() {
        return isValid() && mediaPlayer.isPlaying();
    }

    /**
     * 当前播放进度
     *
     * @return
     */
    private int getCurProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 当前播放器状态
     *
     * @return
     */
    private int getCurState() {
        return curState;
    }

    /**
     * 注销播放器
     */
    public void releasePlayer() {
        stopTask();
        if (beanList != null) {
            beanList.clear();
            beanList = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        curState = STATE_INITIAL;
    }

    private final Handler handler = new Handler(msg -> false);

    private final Runnable getProcessTask = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(getProcessTask, INTERVAL_GET_PROGRESS);
            try {
                if (isPlaying()) {
                    notifyProcessChanged(getCurProgress());
                }
            } catch (Throwable ignored) {
            }
        }
    };

    private void startTask() {
        stopTask();
        handler.post(getProcessTask);
    }

    private void stopTask() {
        handler.removeCallbacks(getProcessTask);
    }

    @Override
    protected void onDetachedFromWindow() {
        releasePlayer();
        super.onDetachedFromWindow();
    }
}
