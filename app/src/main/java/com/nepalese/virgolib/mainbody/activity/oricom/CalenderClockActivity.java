package com.nepalese.virgolib.mainbody.activity.oricom;

import android.os.Bundle;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

/**
 * textclock：
 * 1. 时间格式；
 * 2.
 *
 * calender：
 * 1. 日期、时间选择器；
 */
public class CalenderClockActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_clock);
        init();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        setTextClock();
    }

    private void setTextClock() {
        //每秒更新：
        /**
         * 设置24小时制时：判断系统是否为24小时制
         * textclock.is24HourModeEnabled();
         *
         * 或者在xml同时设置：
         * android:format12Hour="yyyy/MM/dd hh:mm:ss EEEE aa"
         * android:format24Hour="yyyy/MM/dd HH:mm:ss EEEE"
         */

        /**
         * textclock 显示格式：
         * xml
         * android:format12Hour="yyyy/MM/dd hh:mm:ss EEEE a"
         * android:format24Hour="yyyy/MM/dd HH:mm:ss EEEE"
         *
         * y -> 年 yy:后面两位 yyyy:完整年份
         * M -> 月 M:不补零 MM:01-12 MMM:英文缩写 MMMM:英文全写
         * d -> 日 d:不补零 dd:01-31
         * h/H -> 时 h:不补零 hh:00-12 HH:00-23
         * m -> 分
         * s -> 秒
         * E -> 周 E:英文缩写 EEEE:英文全写
         * a -> 上下午 a:a/p  aa:am/pm
         *
         * 设置属性值示例(1970/04/06 3:23am)
         *  MM/dd/yy h:mmaa -> 04/06/70 3:23am
         *  MMM dd, yyyy h:mmaa-> Apr 6, 1970 3:23am
         *  MMMM dd, yyyy h:mmaa -> April 6, 1970 3:23am
         *  E, MMMM dd, yyyy h:mmaa -> Mon, April 6, 1970 3:23am
         *  EEEE, MMMM dd, yyyy h:mmaa -> Monday, April 6, 1970 3:23am
         */

        /**
         * android:timeZone -> 指定要使用的时区，设置后忽略系统时间变化
         */
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void release() {

    }

    @Override
    protected void onBack() {
        finish();
    }
}