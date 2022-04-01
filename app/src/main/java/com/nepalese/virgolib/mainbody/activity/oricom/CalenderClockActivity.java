package com.nepalese.virgolib.mainbody.activity.oricom;

import android.os.Build;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

import androidx.annotation.NonNull;

/**
 * textclock：
 * 1. 时间格式；
 *
 * calendarView：
 * 1. 使用；
 *
 * datepicker, timepicker：
 * 1. 日期、时间选择器；
 */
public class CalenderClockActivity extends BaseActivity {
    private static final String TAG = "CalenderClockActivity";

    private CalendarView calendarView;
    private DatePicker datePicker;
    private TimePicker timePicker1, timePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_clock);
        init();
    }

    @Override
    protected void initUI() {
        calendarView = findViewById(R.id.calendar_cc);
        datePicker = findViewById(R.id.datepicker);
        timePicker1 = findViewById(R.id.timepicker_spinner);
        timePicker2 = findViewById(R.id.timepicker_clock);
    }

    @Override
    protected void initData() {
        setTextClock();
        setCalendar();
        setTimePicker();
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

    private void setCalendar() {
        //日历

        /**
         *  设置每周起始日（默认星期日）
         *  calendarView.setFirstDayOfWeek(Calendar.MONDAY);
         */
    }

    private void setTimePicker() {
        //时间选择器
        timePicker1.setIs24HourView(true);//24小时制
        //设置选中时间， 默认当前时间
        timePicker1.setCurrentHour(9);
        timePicker1.setCurrentMinute(30);
    }

    @Override
    protected void setListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                showToast(year + " - "+ month + " - " + dayOfMonth);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    showToast(year + " - " + monthOfYear + " - " + dayOfMonth);
                }
            });
        }

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                showToast(hourOfDay + " : " + minute);
            }
        });
    }

    @Override
    protected void release() {

    }

    @Override
    protected void onBack() {
        finish();
    }
}