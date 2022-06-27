package com.nepalese.virgolib.mainbody.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.nepalese.virgolib.interfaces.ScreenObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2022/6/27.
 * Usage:锁屏状态：动态监听,单例模式
 */

public class ScreenReceiver extends BroadcastReceiver {
    private static ScreenReceiver instance;

    private final List<ScreenObserver> observerList;

    private static ScreenReceiver getInstance() {
        if (instance == null) {
            synchronized (ScreenReceiver.class) {
                if (instance == null) {
                    instance = new ScreenReceiver();
                }
            }
        }

        return instance;
    }

    public ScreenReceiver() {
        observerList = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
                for (ScreenObserver observer : observerList) {
                    observer.onScreenOn();
                }
                break;
            case Intent.ACTION_SCREEN_OFF:
                for (ScreenObserver observer : observerList) {
                    observer.onScreenOff();
                }
                break;
            case Intent.ACTION_USER_PRESENT:
                for (ScreenObserver observer : observerList) {
                    observer.onScreenUnlock();
                }
                break;
        }
    }

    /**
     * 注册广播：仅需一次
     */
    public static void registerReciver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        context.registerReceiver(getInstance(), filter);
    }

    /**
     * 注销广播
     */
    public static void unregisterReciver(Context context) {
        context.unregisterReceiver(getInstance());
    }

    /**
     * 注册回调：需要的地方监听，需已开启广播监听
     *
     * @param observer ScreenObserver
     */
    public static void registerObserver(ScreenObserver observer) {
        if (observer == null) {
            return;
        }

        if (getInstance().observerList.contains(observer)) {
            //已注册
            return;
        }

        getInstance().observerList.add(observer);
    }

    /**
     * 注销回调
     *
     * @param observer ScreenObserver
     */
    public static void unregisterObserver(ScreenObserver observer) {
        if (observer == null) {
            return;
        }

        if (getInstance().observerList.isEmpty()) {
            return;
        }

        getInstance().observerList.remove(observer);
    }
}
