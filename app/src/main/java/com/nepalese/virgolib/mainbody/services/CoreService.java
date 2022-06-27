package com.nepalese.virgolib.mainbody.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2022/6/27.
 * Usage: 后台服务：定位，屏幕状态变化监听，
 */

public class CoreService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        registerReceiver();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        unRegistertReceiver();
        super.onDestroy();
    }
}
