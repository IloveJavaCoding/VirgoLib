package com.nepalese.virgolib.widget.lrc;

import com.nepalese.virgosdk.Beans.BaseBean;

/**
 * Created by Administrator on 2022/6/29.
 * Usage: 歌词行
 */

public class LrcBean extends BaseBean {
    private long time;
    private String lrc;

    public LrcBean(long time, String lrc) {
        this.time = time;
        this.lrc = lrc;
    }

    public LrcBean() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}
