package com.nepalese.virgolib.widget.lrc;

import android.widget.Toast;

import com.nepalese.virgosdk.Beans.BaseBean;

/**
 * Created by Administrator on 2022/6/29.
 * Usage: 歌词行
 */

public class LrcBean extends BaseBean {
    private final long time;
    private final String lrc;
    private final String strTime;

    public LrcBean(long time, String lrc , String strTime) {
        this.time = time;
        this.lrc = lrc;
        this.strTime = strTime;
    }

    public long getTime() {
        return time;
    }

    public String getLrc() {
        return lrc;
    }

    public String getStrTime() {
        return strTime;
    }

    @Override
    public String toString() {
        return "LrcBean{" +
                "time=" + time +
                ", lrc='" + lrc + '\'' +
                ", strTime='" + strTime + '\'' +
                '}';
    }
}
