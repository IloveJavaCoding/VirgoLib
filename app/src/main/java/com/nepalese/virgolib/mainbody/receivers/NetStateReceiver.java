package com.nepalese.virgolib.mainbody.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.nepalese.virgolib.helper.CommonHelper;
import com.nepalese.virgolib.interfaces.NetObserver;
import com.nepalese.virgolib.interfaces.NetType;
import com.nepalese.virgosdk.Util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2022/3/11.
 * Usage: 网络状态变化监听：动态监听,单例模式
 */

public class NetStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStateReceiver";
     private static NetStateReceiver instance;

     private final List<NetObserver> observerList;

     private static NetStateReceiver getInstance(){
         if(instance==null){
             synchronized (NetStateReceiver.class){
                 if(instance==null){
                     instance = new NetStateReceiver();
                 }
             }
         }

         return instance;
     }

    public NetStateReceiver() {
         observerList = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //todo
        switch (intent.getAction()){
            case ConnectivityManager.CONNECTIVITY_ACTION:
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    int type2 = networkInfo.getType();
                    CommonHelper.LogDebug(TAG,"网络信息：" + networkInfo.toString());
                    switch (type2) {
                        case 0://移动 网络    2G 3G 4G 都是一样的 实测 mix2s 联通卡
                            CommonHelper.LogDebug(TAG,"移动!");
                            break;
                        case 1: //wifi网络
                            CommonHelper.LogDebug(TAG, "wifi!");
                            break;
                        case 9:  //网线连接
                            CommonHelper.LogDebug(TAG,"有线!");
                            break;
                    }
                } else {// 无网络
                    CommonHelper.LogDebug(TAG,"无网络!");
                }
                break;
            case "android.net.ethernet.STATE_CHANGE":

                break;
            case "android.net.ethernet.ETHERNET_STATE_CHANGED":

                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:

                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:

                break;
        }

        notifyObservers(parseNetType(NetworkUtil.getNetworkType(context)));
    }

    private void notifyObservers(NetType netType) {
        if (netType == NetType.NETWORK_NO){
            for (NetObserver observer : observerList){
                observer.onNetDisconnected();
            }
        }else {
            for (NetObserver observer : observerList){
                observer.onNetConnected(netType);
            }
        }
    }

    /**
     * 注册广播：仅需一次
     */
    public static void registerReciver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.net.ethernet.STATE_CHANGE");//有线网络
        filter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(getInstance(), filter);
    }

    /**
     * 注销广播
     */
    public static void unregisterReciver(Context context){
        context.unregisterReceiver(getInstance());
    }

    /**
     * 注册回调：需要的地方监听，需已开启广播监听
     * @param observer NetObserver
     */
    public static void registerObserver(NetObserver observer){
        if (observer == null) {
            return;
        }

        if(getInstance().observerList.contains(observer)){
            //已注册
            return;
        }

        getInstance().observerList.add(observer);
    }

    /**
     * 注销回调
     * @param observer NetObserver
     */
    public static void unregisterObserver(NetObserver observer){
        if (observer == null) {
            return;
        }

        if(getInstance().observerList.isEmpty()){
            return;
        }

        getInstance().observerList.remove(observer);
    }

    private NetType parseNetType(int type) {
        switch (type) {
            case -1:
                return NetType.NETWORK_NO;
            case 0:
                return NetType.NETWORK_ETHERNET;
            case 1:
                return NetType.NETWORK_WIFI;
            case 2:
                return NetType.NETWORK_2G;
            case 3:
                return NetType.NETWORK_3G;
            case 4:
                return NetType.NETWORK_4G;
            case 5:
                return NetType.NETWORK_5G;
            default:
                return NetType.NETWORK_UNKNOWN;
        }
    }
}
