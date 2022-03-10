package com.nepalese.virgolib.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nepalese.virgolib.bean.CrashHandler;
import com.nepalese.virgolib.helper.CommonHelper;

/**
 * Created by Administrator on 2022/3/10.
 * Usage:全局
 * app一般启动顺序为：
 * Application->attachBaseContext ==> ContentProvider->onCreate ==> Application->onCreate;
 */
public class MyApp extends Application {
    private static final String TAG = "MyApp";

    //全局上下文环境
    private static MyApp myApp;

    /**
     * 单例模式
     *
     * @return Application
     */
    public static MyApp getInstance() {
        if (myApp == null) {
            synchronized (MyApp.class) {
                if (myApp == null) {
                    myApp = new MyApp();
                }
            }
        }
        return myApp;
    }

    public MyApp() {
        myApp = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG, "attachBaseContext: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");

        init();
    }

    /**
     * 1. 未捕获异常回调监听：运行时异常、
     * 2. 内存泄漏监听安装；
     * 3. OkGo 下载模块初始化；
     * 4. 内存不足监听；
     */
    private void init() {
        catchCrash();
        catchLeak();

//        OkGoManager.initOkGo(this);
    }

    private void catchCrash() {
        CrashHandler.getInstance(this).setErrorLogDir("xxx");
    }

    /**
     * LeakCanary 一个简单方便的内存泄漏检测框架, 2.0 不需要这个
     * debugImplementation('com.squareup.leakcanary:leakcanary-android:1.5.4')
     * releaseImplementation('com.squareup.leakcanary:leakcanary-android-no-op:1.5.4')
     */
    private void catchLeak() {
        // 如果是在HeapAnalyzer进程里，则跳过，因为该进程是专门用来堆内存分析的。
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            //调用LeakCanary.install()的方法来进行必要的初始化工作，来监听内存泄漏。
//            LeakCanary.install(this);
//        }
    }

    //只要低内存状态下,就会回调
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(TAG, "可用内存不足: " + CommonHelper.getMemInfo(this));
        Runtime.getRuntime().gc();
    }
}