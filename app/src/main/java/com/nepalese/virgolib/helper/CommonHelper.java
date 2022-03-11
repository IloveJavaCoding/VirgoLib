package com.nepalese.virgolib.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.nepalese.virgosdk.Util.JsonUtil;

/**
 * Created by Administrator on 2022/3/9.
 * Usage: 临时通用工具类，后面与sdk合并
 */

public class CommonHelper {

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
     * 跳转到设置应用详情页
     * @param activity
     */
    public static void jump2AppDetail(Activity activity) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
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
}
