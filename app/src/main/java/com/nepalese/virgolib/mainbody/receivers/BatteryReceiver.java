package com.nepalese.virgolib.mainbody.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2022/3/10.
 * Usage:电源、电量变化广播
 */

public class BatteryReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, action);

        if (TextUtils.isEmpty(action)) return;

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);//当前电池电量
//        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);//电池容量

        switch (action) {
            case Intent.ACTION_BATTERY_CHANGED:
                //电量变化
                Log.i(TAG, "电量变化: " + level);
                break;
            case Intent.ACTION_BATTERY_LOW:
                //电量低
                Log.i(TAG, "电量低: " + level);
                break;
            case Intent.ACTION_BATTERY_OKAY:
                //电量充满
                Log.i(TAG, "电量充满: ");
                break;
            case Intent.ACTION_POWER_CONNECTED:
                //电源连接
                Log.i(TAG, "电源连接: ");
                judgeChargeType(intent);
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                //电源断开
                Log.i(TAG, "电源断开: ");
                break;
        }
    }

    /**
     * 判断充电方式
     * @param intent
     */
    private void judgeChargeType(Intent intent) {
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (chargePlug){
            case BatteryManager.BATTERY_PLUGGED_USB:
                Log.i(TAG, "judgeChargeType: USB");
                break;
            case BatteryManager.BATTERY_PLUGGED_AC:
                Log.i(TAG, "judgeChargeType: AC charger");
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                Log.i(TAG, "judgeChargeType: 无线");
                break;
        }
    }
}
