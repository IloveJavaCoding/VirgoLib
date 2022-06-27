package com.nepalese.virgolib.interfaces;

/**
 * Created by Administrator on 2022/6/27.
 * Usage:网络状态变化监听回调
 */

public interface NetObserver {
    void onNetDisconnected();//断开连接
    void onNetConnected(NetType netType);//网络重新连接
}
