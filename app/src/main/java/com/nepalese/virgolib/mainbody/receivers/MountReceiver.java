package com.nepalese.virgolib.mainbody.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2022/3/10.
 * Usage: U盘/sd card 插入监听
 */
public class MountReceiver extends BroadcastReceiver {
    private static final String TAG = "MountReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, action);

        if (TextUtils.isEmpty(action)) return;

        if(intent.getData() == null) return;

        switch (action) {
            case Intent.ACTION_MEDIA_UNMOUNTED:
            case Intent.ACTION_MEDIA_REMOVED:
            case Intent.ACTION_MEDIA_EJECT:
                //拔出
                Log.i(TAG, "拔出: ");
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                //插入
                String path = intent.getData().getPath();
                Log.i(TAG, "插入: " + path);
                if(path.contains("emulated")){//防止自启时本地路径触发
                    return;
                }
                break;
        }
    }
}
