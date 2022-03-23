package com.nepalese.virgolib.mainbody.activity.oricom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.widget.camera1.CameraHelper;
import com.nepalese.virgolib.widget.camera1.CameraListener;
import com.nepalese.virgosdk.Base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 相机功能：需相机权限 <uses-permission android:name="android.permission.CAMERA" />
 * camera: <= android 5.0
 */
public class CameraActivity extends BaseActivity {
    private static final String TAG = "CameraActivity";

    private CameraHelper cameraHelper;
    private TextureView textureView;
    private ImageView imageView;
    private boolean captureNow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();
    }

    @Override
    protected void initUI() {
        textureView = findViewById(R.id.texture_camera);
        imageView = findViewById(R.id.img_camera_capture);
    }

    @Override
    protected void initData() {
        initCamera();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void release() {
        releaseCamera();
    }

    @Override
    protected void onBack() {
        finish();
    }

    /**
     * 打开、暂停相机
     * @param view
     */
    public void onPlayPauseCamera(View view) {
        playOrPauseCamera();
    }

    /**
     * 拍照
     * @param view
     */
    public void onCaptureImg(View view) {
        captureNow = true;
    }

    /**
     * 录像
     * @param view
     */
    public void onRecordVideo(View view) {

    }

    //=========================================camera1==========================================
    /**
     * 初始化相机
     */
    private void initCamera() {
        cameraHelper = new CameraHelper.Builder()
                .viewSize(new Point(textureView.getMeasuredWidth(), textureView.getMeasuredHeight()))
                .rotation(0)
                .cameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(textureView)
                .cameraListener(cameraListener1)
                .build();
        cameraHelper.init();
    }

    /**
     * 播放、暂停
     */
    private void playOrPauseCamera(){
        if(cameraHelper!=null){
            if(cameraHelper.isStopped()){
                cameraHelper.openCamera();
            }else{
                cameraHelper.stopCamera();
            }
        }
    }

    /**
     * 注销
     */
    private void releaseCamera(){
        if (cameraHelper != null) {
            cameraHelper.releaseCamera();
            cameraHelper = null;
        }
    }

    private final CameraListener cameraListener1 = new CameraListener() {
        @Override
        public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {}

        @Override
        public void onPreview(byte[] data, Camera camera) {
            if(captureNow){
                Log.i(TAG, "拍照：");
                captureNow = false;
                imageView.setImageBitmap(captureImg(data,
                        camera.getParameters().getPreviewSize().width,
                        camera.getParameters().getPreviewSize().height));

//                saveImageLocal(data,
//                        camera.getParameters().getPreviewSize().width,
//                        camera.getParameters().getPreviewSize().height,
//                        Environment.getExternalStorageDirectory().getPath() + "/Pictures");
            }
        }

        @Override
        public void onCameraClosed() {
            Log.d(TAG, "onCameraClosed: ");
            releaseCamera();
        }

        @Override
        public void onCameraError(Exception e) {
            Log.e(TAG, "onCameraError: ");
            releaseCamera();
        }

        @Override
        public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {}
    };

    /**
     * 将相机原始数据加工成图片
     * @param data 从相机内获取的原始数据 nv21
     * @param width 相机预览宽 camera.getParameters().getPreviewSize().width
     * @param height 相机预览高
     */
    private Bitmap captureImg(byte[] data, int width, int height){
        YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(data.length);
        if(!image.compressToJpeg(new Rect(0, 0, width, height), 100, arrayOutputStream)){
            //数据转换失败
            return null;
        }
        byte[] bytes = arrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }

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
}