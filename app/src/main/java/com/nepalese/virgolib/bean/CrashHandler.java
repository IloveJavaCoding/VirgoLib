package com.nepalese.virgolib.bean;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Created by Administrator on 2022/3/10.
 * Usage: 自定义未捕获异常处理类
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    @SuppressLint("StaticFieldLeak")
    private static CrashHandler instance;
    private Context context;
    private String errorLogDir;//错误日志输出目录

    //单例模式
    public static CrashHandler getInstance(Context context){
        if(instance==null){
            synchronized (CrashHandler.class){
                if(instance==null){
                    instance = new CrashHandler(context);
                }
            }
        }

        return instance;
    }

    private CrashHandler(Context context) {
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context;
    }

    public String getErrorLogDir() {
        return errorLogDir;
    }

    public void setErrorLogDir(String errorLogDir) {
        this.errorLogDir = errorLogDir;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        try{
            //处理异常信息
            dealExceptionInfo(e);
        }catch (Exception exception){
            //
        }

        //退出
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void dealExceptionInfo(Throwable e) {
        //todo

    }
}
