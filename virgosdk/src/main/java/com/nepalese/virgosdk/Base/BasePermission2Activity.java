package com.nepalese.virgosdk.Base;

import android.content.pm.PackageManager;
import android.os.Build;

import com.nepalese.virgosdk.Util.SystemUtil;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * @author nepalese on 2022/03/11
 * @usage 需要申请权限的activity: 仅弹框式授权，不包含跳转至设置页
 */
public abstract class BasePermission2Activity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 0x01;
    private final String[] needPermissions;

    public BasePermission2Activity(String[] permissions) {
        this.needPermissions = permissions;
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(checkPermissions()){
            onPermissioned();
        }else{
            onPermissionDeny();
        }
    }

    /**
     * 授权成功
     */
    abstract void onPermissioned();

    /**
     * 拒绝授权
     */
    abstract void onPermissionDeny();

    private boolean checkPermissions() {
        if(needPermissions==null || needPermissions.length<1){
            return true;
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //Android 6+ 需动态申请权限
            if(!SystemUtil.checkPermission(this, needPermissions)){
                ActivityCompat.requestPermissions(this, needPermissions, PERMISSION_REQUEST_CODE);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }

            if (isAllGranted) {
                onPermissioned();
            } else {
                onPermissionDeny();
            }
        }
    }
}