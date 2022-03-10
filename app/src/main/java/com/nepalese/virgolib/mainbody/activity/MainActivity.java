package com.nepalese.virgolib.mainbody.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.helper.CommonHelper;
import com.nepalese.virgolib.helper.GlideHelper;
import com.nepalese.virgosdk.Util.SystemUtil;
import com.nepalese.virgosdk.Util.WinowUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //进入时需申请的权限
    private static final String[] NEEDED_PERMISSIONS_6 = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String[] NEEDED_PERMISSIONS_10 = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_PERMISSIONS_STORAGE = 0x01;
    private static final int REQUEST_PERMISSIONS_ALL_FILES = 0x02;
    private static final int REQUEST_PERMISSIONS_OVERLAYERS = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        //1. 权限校验
        if(checkPermissions()){
            initData();
        }
    }

    /**
     * 常用权限：
     * 文件读写：（进入申请）
     *  permission:android.permission.READ_EXTERNAL_STORAGE
     *  permission:android.permission.WRITE_EXTERNAL_STORAGE
     *  permission:android.permission.MANAGE_EXTERNAL_STORAGE（Android 11, 需跳转至设置页授权）
     *
     * 网络访问/蓝牙/自启广播：（仅需注册）
     *  permission:android.permission.INTERNET
     *  permission:android.permission.ACCESS_WIFI_STATE
     *  permission:android.permission.ACCESS_NETWORK_STATE
     *  permission:android.permission.BLUETOOTH
     *  permission:android.permission.BLUETOOTH_ADMIN
     *  permission:android.permission.RECEIVE_BOOT_COMPLETED
     *
     * 位置信息：（使用时申请）
     *  permission:android.permission.ACCESS_FINE_LOCATION
     *  permission:android.permission.ACCESS_COARSE_LOCATION
     *
     * 相机：（使用时申请）
     *  permission:android.permission.CAMERA
     *
     * 录音：（使用时申请）
     *  permission:android.permission.RECORD_AUDIO
     *
     * 全局弹框，显示在其他程序顶部：（（进入申请）>=23， 需跳转至设置页授权）
     *   permission:android.permission.SYSTEM_ALERT_WINDOW
     *   permission:android.permission.SYSTEM_OVERLAY_WINDOW
     */
    private boolean checkPermissions() {
        //    Android 6.0以前：只需在AndroidManifest注册使用的权限即可使用；
        //    Android 6.0+（M 23）：正常权限，注册后，系统会自动授予该权限；
        //                        危险权限，部分需动态申请权限（弹框，有些需跳转到设置页手动授权)；
        //                        权限组以一盖全；
        //    Android 10+ (Q 29)：权限按单个分配，不再按组分配；
        //                        app 对自身内/外部文件访问不需要权限申请；
        //                        访问别的应用保存在公有目录下的文件需读取权限；
        //                        对后台应用可启动 Activity进行限制, 当App的Activity不在前台时，其启动Activity会被系统拦截，导致无法启动，
        //                        自启 需申请 SYSTEM_ALERT_WINDOW 权限；
        //    Android 11+ (R 30)：对自身文件不需要申请权限；
        //                        访问外部其他文件可申请 MANAGE_EXTERNAL_STORAGE；（WRITE_EXTERNAL_STORAGE 已无效）

        //申请文件访问权限
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            //android 11+
            if(!Environment.isExternalStorageManager()){
                //跳转新页面申请权限
                CommonHelper.jump4Permission(this, Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION, REQUEST_PERMISSIONS_ALL_FILES);
                return false;
            }
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            //Android 10 WRITE_EXTERNAL_STORAGE 没用
            if(!CommonHelper.checkPermission(this, NEEDED_PERMISSIONS_10)){
                ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS_10, REQUEST_PERMISSIONS_STORAGE);
                return false;
            }
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //Android 6+ 需动态申请权限
            if(!CommonHelper.checkPermission(this, NEEDED_PERMISSIONS_6)){
                ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS_6, REQUEST_PERMISSIONS_STORAGE);
                return false;
            }
        }

        return checkOverPermission();
    }

    private boolean checkOverPermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //顶部显示权限
            if (!Settings.canDrawOverlays(this)) {
                CommonHelper.jump4Permission(this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION, REQUEST_PERMISSIONS_OVERLAYERS);
                return false;
            }
        }
        return true;
    }

    private void initData() {
        //正常进入
        Log.i(TAG, ": 正常进入!");

        //隐藏顶部状态栏
        WinowUtil.setStatusHide(this);

        ImageView imageView = findViewById(R.id.imgMainBg);

        if(CommonHelper.isLandscape(this)){
            GlideHelper.loadImage(R.raw.img_bg_land, imageView);
        }else{
            GlideHelper.loadImage(R.raw.img_bg_portrait, imageView);
        }

        jump2Home();
    }

    private void jump2Home() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SystemUtil.jumActivity(MainActivity.this, HomeActivity.class);
                finish();
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PERMISSIONS_ALL_FILES){
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R){
                if (Environment.isExternalStorageManager()) {
                    checkOverPermission();
                }else{
                    SystemUtil.showToast(this, "访问所有文件权限申请失败!");
                    finish();
                }
            }
        }else if(requestCode==REQUEST_PERMISSIONS_OVERLAYERS){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if (Settings.canDrawOverlays(this)) {
                    initData();
                }else{
                    SystemUtil.showToast(this, "可显示在应用顶部权限申请失败!");
                    finish();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
        }

        if (requestCode == REQUEST_PERMISSIONS_STORAGE) {
            if (isAllGranted) {
                checkOverPermission();
            } else {
                SystemUtil.showToast(this, "文件读写权限申请失败!");
                finish();
            }
        }
    }
}