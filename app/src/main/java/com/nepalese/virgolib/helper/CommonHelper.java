package com.nepalese.virgolib.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

import com.nepalese.virgosdk.Util.JsonUtil;
import com.nepalese.virgosdk.Util.ScreenUtil;
import com.nepalese.virgosdk.Util.SystemUtil;

import androidx.core.content.ContextCompat;

/**
 * Created by Administrator on 2022/3/9.
 * Usage: 临时通用工具类，后面与sdk合并
 */

public class CommonHelper {
    /**
     * 校验申请的权限是否都已授权
     * @param context
     * @param permissions 申请的权限
     * @return
     */
    public static boolean checkPermission(Context context, String[] permissions) {
        if (permissions != null && permissions.length > 0) {
            boolean allGranted = true;

            for (String neededPermission : permissions) {
                allGranted &= ContextCompat.checkSelfPermission(context, neededPermission) == 0;
            }

            return allGranted;
        } else {
            return true;
        }
    }

    /**
     * activity 跳转到设置页请求某权限
     * @param activity
     * @param action 权限
     * @param code 请求码
     */
    public static void jump4Permission(Activity activity, String action, int code){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 获取当前可用内存
     * @param context
     * @return
     */
    public static long getFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return info.availMem;
    }

    /**
     * 当前内存详细信息
     * @param context
     * @return
     */
    public static String getMemInfo(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return JsonUtil.toJson(info);
    }

    /**
     * 判断横屏还是竖屏
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context){
        DisplayMetrics screenDM;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            screenDM = ScreenUtil.getScreenDM(context);
        }else{
            screenDM = ScreenUtil.getScreenDMOld(context);
        }

        return screenDM.widthPixels>screenDM.heightPixels;
    }
}
