package com.nepalese.virgolib.bean;

import com.nepalese.virgosdk.Beans.BaseBean;

/**
 * Created by Administrator on 2022/3/18.
 * Usage: 简单自定义音频文件bean类
 */

public class AudioBean extends BaseBean {
    private String name;//歌名
    private String displayName;//显示名（文件名去后缀）
    private String artist;//歌手名
    private String path;//文件路径
    private int duration;//时长
    private long size;//文件大小

    public AudioBean() {
    }

    public AudioBean(String path) {
        //
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "AudioBean{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", artist='" + artist + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }
}
