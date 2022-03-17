package com.nepalese.virgolib.mainbody.activity.oricom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.widget.camera2.Camera2Helper;
import com.nepalese.virgolib.widget.camera2.Camera2Listener;
import com.nepalese.virgosdk.Base.BasePermission2Activity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

/**
 * 相机功能：需相机权限 <uses-permission android:name="android.permission.CAMERA" />
 * camera2: android 5.0+
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Activity extends BasePermission2Activity {
    private static final String TAG = "Camera2Activity";

    private Camera2Helper camera2Helper;
    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
    }

    @Override
    protected void setNeedPermissions() {
        needPermissions = new String[]{
                Manifest.permission.CAMERA
        };
    }

    @Override
    protected void onPermissioned() {
        init();
    }

    @Override
    protected void onPermissionDeny() {
        finish();
    }

    @Override
    protected void initUI() {
        textureView = findViewById(R.id.texture_camera2);
    }

    @Override
    protected void initData() {
        initCamera();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        releaseCamera();
        super.onDestroy();
    }

    private void releaseCamera() {
        if (camera2Helper != null) {
            camera2Helper.releaseCamera();
            camera2Helper = null;
        }
    }

    /**
     * 打开、暂停相机
     * @param view
     */
    public void onPlayPauseCamera(View view) {
        if (camera2Helper != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(camera2Helper.isStoped()){
                camera2Helper.openCamera();
            }else{
                camera2Helper.stopCamera();
            }
        }
    }

    /**
     * 拍照
     * @param view
     */
    public void onCaptureImg(View view) {

    }

    /**
     * 录像
     * @param view
     */
    public void onRecordVideo(View view) {

    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        camera2Helper = new Camera2Helper.Builder(this)
                .viewSize(new Point(textureView.getMeasuredWidth(), textureView.getMeasuredHeight()))
                .isFrontCamres(false)
                .isMirror(false)
                .previewOn(textureView)
                .setCamera2Listener(camera2Listener)
                .build();
        camera2Helper.init();
    }

    private final Camera2Listener camera2Listener = new Camera2Listener() {
        @Override
        public void onCameraOpened(String cameraId, boolean isMirror) {

        }

        @Override
        public void onPreview(byte[] data) {

        }

        @Override
        public void onCameraClosed() {

        }

        @Override
        public void onCameraError(String msg) {

        }
    };
}