package com.nepalese.virgolib.widget.vitamioplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.Map;


public class BaseSurfaceVideoPlayer extends SurfaceView {
    private static final String TAG = "BaseVideoPlayer";

    // all possible internal states
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private Context context;
    private Uri mUri;
    private Map<String, String> mHeaders;

    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener;

    private int mCurrentState;
    private int mTargetState;
    private int mSeekWhenPrepared;
    private int mCurrentBufferPercentage;

    private float volumeLeft;
    private float volumeRight;
    private boolean needMute;

    public BaseSurfaceVideoPlayer(Context context) {
        this(context, null);
    }

    public BaseSurfaceVideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSurfaceVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPlayer();
    }

    private void initPlayer() {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        getHolder().addCallback(surfaceCallback);

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        volumeLeft = -1;
        volumeRight = -1;
        mSeekWhenPrepared = 0;

        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
    }

    //1. 声音控制=============================================================
    public void doMute(boolean needMute) {
        this.needMute = needMute;
        doMute();
    }

    private void doMute() {
        try {
//            if (VersionHelper.isBuild3798()) {
//                if (mMediaPlayer != null) {
//                    if (needMute) {
//                        new HiMediaPlayer(mMediaPlayer).setAudioChannel(7);
//                    } else {
//                        new HiMediaPlayer(mMediaPlayer).setAudioChannel(0);
//                    }
//                }
//            }
        } catch (Throwable e) {
            Log.e(TAG, "静音失败: " + e.getMessage());
        }
    }

    public void setVolume(float volumeLeft, float volumeRight) {
        this.volumeLeft = volumeLeft;
        this.volumeRight = volumeRight;
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(volumeLeft, volumeRight);
        }
    }

    //2. 设置播放资源==========================================================
    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    //3. 播放控制===========================================================
    public void start() {
        if (isInPlaybackState()) {
            Log.i(TAG, "start: ");
            mCurrentState = STATE_PLAYING;
            mMediaPlayer.start();
        }
        mTargetState = STATE_PLAYING;
    }

    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mCurrentState = STATE_PAUSED;
                mMediaPlayer.pause();
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }

        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mSeekWhenPrepared = 0;
            mMediaPlayer.seekTo(msec);
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    public void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
            mAudioManager.abandonAudioFocus(null);
        }
    }

    //4. 设置监听==========================================================
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener) {
        mOnVideoSizeChangeListener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;

        openVideo();
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            Log.i(TAG, "openVideo: fail");
            return;
        }

        try {
            if (mMediaPlayer == null) {
                Log.i(TAG, "new MediaPlayer!");
                mMediaPlayer = new MediaPlayer();
            } else {
                mMediaPlayer.reset();
            }

            mCurrentBufferPercentage = 0;

            // a context for the subtitle renderers
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);

            mMediaPlayer.setDataSource(getContext(), mUri, mHeaders);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            mCurrentState = STATE_PREPARING;
        } catch (IOException ex) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        } catch (IllegalArgumentException ex) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    private final MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (mOnVideoSizeChangeListener != null) {
                mOnVideoSizeChangeListener.onVideoSizeChanged(mp, width, height);
            }
        }
    };

    private final MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.i(TAG, "onPrepared: ");
            mCurrentState = STATE_PREPARED;

            if (volumeLeft != -1 && volumeRight != -1) {
                mp.setVolume(volumeLeft, volumeRight);
            }

            doMute();

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }

            // mSeekWhenPrepared may be changed after seekTo() call
            int seekToPosition = mSeekWhenPrepared;
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }

            if (mTargetState == STATE_PLAYING) {
                start();
            }
        }
    };

    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;

            mAudioManager.abandonAudioFocus(null);
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mMediaPlayer);
            }
        }
    };


    private final MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;

            Log.e(TAG, "onError: " + framework_err );
            if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                    return true;
                }
            }
            return true;
        }
    };

    //网络流媒体的缓冲监听: 播放在线资源时触发
    private final MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
//            Log.i(TAG, "onBufferingUpdate: " + percent);
        }
    };


    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mMediaPlayer != null && (mTargetState == STATE_PLAYING)) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared);
                }
                start();
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated: ");
            mSurfaceHolder = holder;
            openVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            release();
        }
    };

    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
            mAudioManager.abandonAudioFocus(null);
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return VideoView.class.getName();
    }
}
