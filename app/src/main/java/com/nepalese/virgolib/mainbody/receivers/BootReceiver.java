package com.nepalese.virgolib.mainbody.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2022/3/10.
 * Usage: 接收自启广播，实现自启
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(TextUtils.isEmpty(action)) return;

        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.i(TAG, "onReceive: 自启");
            Message obMessage = mHandler.obtainMessage();
            obMessage.obj = context;
            mHandler.sendMessageDelayed(obMessage, 5000);
        }
    }

    private final Handler mHandler = new Handler(msg -> {
        Context context = (Context) msg.obj;
        //todo 启动app
        return false;
    });
}
