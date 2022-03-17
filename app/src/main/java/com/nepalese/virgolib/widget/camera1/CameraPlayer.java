package com.nepalese.virgolib.widget.camera1;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2021/12/7.
 * Usage: 相机
 */
public class CameraPlayer extends FrameLayout {
    private static final String TAG = "CameraPlayer";

    private final Context context;
    private TextureView view;
    private CameraHelper cameraHelper;
    private boolean hasInited;
    private boolean captureNow;

    public CameraPlayer(@NonNull Context context) {
        this(context, null);
    }

    public CameraPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        hasInited = false;
        captureNow = false;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        int mWidth = params.width;
        int mHeight = params.height;
        if(mWidth<0 || mHeight<0){//创建时为负数
            return;
        }
        init(mWidth, mHeight);
    }

    private void init(int w, int h) {
        if(!hasInited){
            Log.i(TAG, "init camera");
            hasInited = true;
            removeAllViews();
            view = new TextureView(context);
            LayoutParams layoutParams = new LayoutParams(w, h);
            this.addView(view, layoutParams);
        }
    }

    private final CameraListener cameraListener = new CameraListener() {
        @Override
        public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {}

        @Override
        public void onPreview(byte[] data, Camera camera) {
            if(captureNow){
                Log.i(TAG, "拍照：");
                captureNow = false;

                saveImageLocal(data,
                        camera.getParameters().getPreviewSize().width,
                        camera.getParameters().getPreviewSize().height,
                        Environment.getExternalStorageDirectory().getPath() + "/Pictures");
            }
        }

        @Override
        public void onCameraClosed() {
            Log.d(TAG, "onCameraClosed: ");
            release();
        }

        @Override
        public void onCameraError(Exception e) {
            Log.e(TAG, "onCameraError: ");
            release();
        }

        @Override
        public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {}
    };

    /**
     * 拍照存储到本地
     * @param data 相机原始数据 nv21
     * @param width 相机预览宽 camera.getParameters().getPreviewSize().width
     * @param height 相机预览高
     * @param dir 保存路径（文件夹）
     */
    private void saveImageLocal(byte[] data, int width, int height, String dir){
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
        //以当前时间命名
        String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date()) + ".jpg";
        File imgFile = new File(dir, fileName);
        if(!imgFile.exists()){
            try {
                if(imgFile.createNewFile()){
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
                    FileOutputStream outputStream = new FileOutputStream(imgFile);
                    image.compressToJpeg(new Rect(0, 0, width, height), 100, outputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 初始化相机
     */
    public void initCamera() {
        cameraHelper = new CameraHelper.Builder()
                .viewSize(new Point(view.getMeasuredWidth(), view.getMeasuredHeight()))
                .rotation(0)
                .cameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(view)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
    }

    /**
     * 开始播放
     */
    public void startPlay(){
        if(cameraHelper!=null){
            cameraHelper.openCamera();
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay(){
        if (cameraHelper != null) {
            cameraHelper.stopCamera();
        }
    }

    /**
     * 继续播放
     */
    public void continuePlay(){
        if (cameraHelper != null && cameraHelper.isStopped()) {
            cameraHelper.openCamera();
        }
    }

    /**
     * 注销
     */
    public void release(){
        hasInited = false;
        if (cameraHelper != null) {
            cameraHelper.releaseCamera();
            cameraHelper = null;
        }
        if(view!=null){
            view = null;
        }
    }
}
