package com.nepalese.virgolib.widget.camera2;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

/**
 * Created by Administrator on 2022/3/17.
 * Usage:camera2
 * 相对于camera ，camera2 在Api上将拍照对象进行了独立，camera2采用pipeline的方式，
 * 将Camera 设备和 Android 设备连接起来，Android Device通过管道发送CaptureRequest拍照请求给Camera Device，
 * Camera Device通过管道返回CameraMetadata数据给Android Device，这一切都发生在CameraCaptureSession的会话中。
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Helper {
    private static final String TAG = "Camera2Helper";
    private static final Object TAG_PREVIEW = "PREVIEW";

    private Context context;
    private CameraManager cameraManager;//所有相机设备（CameraDevice）的管理者, 用于检测、打开系统的摄像头
    //相机实例
    //通过 reateCaptureRequest (int templateType)方法创建 CaptureRequest.Builder
    // TEMPLATE_PREVIEW ：预览
    // TEMPLATE_RECORD：拍摄视频
    // TEMPLATE_STILL_CAPTURE：拍照
    // TEMPLATE_VIDEO_SNAPSHOT：创建视视频录制时截屏的请求
    private CameraDevice cameraDevice;

    private CameraCaptureSession captureSession;//用于创建预览、拍照的Session类
    private CaptureRequest.Builder captureRequestBuilder;//预览请求, 需重复调用以更新页面
    private CaptureRequest captureRequest;//

    private View view;//显示画布
    private Point viewSize;//显示画布大小
    private Camera2Listener camera2Listener;

    private boolean isFrontCamera;//设置的相机 是否为前置，默认后置
    private boolean isMirror;//镜像显示, 仅在textureView上有效

    private String curCameraId;//实际打开的相机id, string

    public Camera2Helper(Builder builder) {
        this.context = builder.context;
        this.view = builder.view;
        this.camera2Listener = builder.camera2Listener;
        this.isFrontCamera = builder.isFrontCamera;

        if (builder.view instanceof TextureView) {
            this.isMirror = builder.isMirror;
        } else {
            this.isMirror = false;
        }
    }

    public void init(){
        if (view instanceof TextureView) {
            ((TextureView) this.view).setSurfaceTextureListener(textureListener);
        } else if (view instanceof SurfaceView) {
            ((SurfaceView) view).getHolder().addCallback(surfaceCallback);
        }

        //镜像显示
        if (isMirror) {
            view.setScaleX(-1);
        }

        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

    }

    @RequiresPermission("android.permission.CAMERA")
    public void openCamera(){
//        //android 6.0+ 需动态申请权限, 仅校验
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                throw new RuntimeException("没有相机访问权限！");
//            }
//        }

        //相机数量相机ID

        try{
            //第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            cameraManager.openCamera(curCameraId, stateCallback, null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    public boolean isStoped(){
        return cameraDevice == null;
    }

    public void stopCamera(){
        if(cameraDevice!=null){
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    public void releaseCamera(){
        stopCamera();
        view = null;
        curCameraId = null;
        viewSize = null;
        camera2Listener = null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 相机 改变状态时候调用
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.i(TAG, "打开摄像头: ");
            cameraDevice = camera;
            if(camera2Listener!=null){
                camera2Listener.onCameraOpened(curCameraId, isMirror);
            }

            //开启预览
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.i(TAG, "摄像头关闭: ");
            if(camera2Listener!=null){
                camera2Listener.onCameraClosed();
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.i(TAG, "发生错误: " + error);
            if(camera2Listener!=null){
                camera2Listener.onCameraError("发生错误: " + error);
            }
        }
    };

    private void startPreview() {
        //设置预览承载页
        Surface surface;
        if (view instanceof TextureView) {
            SurfaceTexture surfaceTexture = ((TextureView) view).getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(viewSize.x, viewSize.y);
            surface = new Surface(surfaceTexture);
        } else {//surface view
            surface = ((SurfaceView) view).getHolder().getSurface();
        }

        try {
            //预览请求的Builder
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            //设置预览的显示界面
            captureRequestBuilder.addTarget(surface);

            MeteringRectangle[] meteringRectangles = captureRequestBuilder.get(CaptureRequest.CONTROL_AF_REGIONS);
            if (meteringRectangles != null && meteringRectangles.length > 0) {
                Log.e("LEE", "PreviewRequestBuilder: AF_REGIONS=" + meteringRectangles[0].getRect().toString());
            }
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);


            // 创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，
            // 第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    captureSession = session;
                    repeatPreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
    private void repeatPreview() {
        captureRequestBuilder.setTag(TAG_PREVIEW);
        captureRequest = captureRequestBuilder.build();
        try {
            //控制预览界面
            captureSession.setRepeatingRequest(captureRequest, captureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置相机基本信息
     * @param width
     * @param height
     */
    private void setCameraConfig(int width, int height){
        try {
            // 遍历所有摄像头
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                if(isFrontCamera){
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                        //前置摄像头
                        curCameraId = cameraId;
                    }else{
                        //后置摄像头
                        continue;
                    }
                }else{
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                        //前置摄像头
                        continue;
                    }else{
                        //后置摄像头
                        curCameraId = cameraId;
                    }
                }
                break;
            }

            if(curCameraId!=null){
//                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(curCameraId);
                // 获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
//                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
            }else{
                if(camera2Listener!=null){
                    camera2Listener.onCameraError("没有找到摄像头！");
                }
                throw new RuntimeException("没有找到摄像头, 换个试一下！");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    //捕获图像成功、失败、进行时等情况的处理；
    private final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Log.i(TAG, "onCaptureCompleted: ");
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private final TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Log.i(TAG, "onSurfaceTextureAvailable: " + width + ", " + height);
            if(viewSize==null){
                //未设置画布大小，使用实际获取的大小
                viewSize = new Point(width, height);
            }

            setCameraConfig(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {}

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            stopCamera();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            if (mCamera != null) {
//                try {
//                    mCamera.setPreviewDisplay(holder);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopCamera();
        }
    };

    public static final class Builder{
        private Context context;//上下文环境
        /**
         * 预览显示的view，目前仅支持surfaceView和textureView
         */
        private View view;

        /**
         * 显示画布大小
         */
        private Point viewSize;

        /**
         *是否为前置摄像头， 默认后置
         */
        private boolean isFrontCamera = false;

        /**
         * 是否镜像显示，只支持textureView
         */
        private boolean isMirror = false;

        /**
         * 事件回调
         */
        private Camera2Listener camera2Listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder previewOn(View val) {
            if (val instanceof TextureView || val instanceof SurfaceView) {
                view = val;
                return this;
            } else {
                throw new RuntimeException("you must preview on a textureView or a surfaceView");
            }
        }

        public Builder viewSize(Point val) {
            viewSize = val;
            return this;
        }

        public Builder isFrontCamres(boolean val) {
            isFrontCamera = val;
            return this;
        }

        public Builder isMirror(boolean val) {
            isMirror = val;
            return this;
        }

        public Builder setCamera2Listener(Camera2Listener camera2Listener) {
            this.camera2Listener = camera2Listener;
            return this;
        }


        public Camera2Helper build() {
            if (viewSize == null) {
                Log.e(TAG, "viewSize is null, now use default previewSize");
            }
            if (camera2Listener == null) {
                Log.e(TAG, "cameraListener is null, callback will not be called");
            }
            if (view == null) {
                throw new RuntimeException("you must preview on a textureView or a surfaceView");
            }
            return new Camera2Helper(this);
        }
    }
}
