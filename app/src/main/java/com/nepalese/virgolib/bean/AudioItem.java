package com.nepalese.virgolib.bean;

import com.nepalese.virgosdk.Beans.BaseBean;

/**
 * Created by Administrator on 2022/3/21.
 * Usage: 简单音频信息类
 */

public class AudioItem extends BaseBean {
    private String name;
    private String path;

    public AudioItem(String path) {
        this.path = path;
        //xxx/Music/生命河灵粮堂 - 全然为祢.mp3
        String displayName = path.substring(path.lastIndexOf("/") + 1);//生命河灵粮堂 - 全然为祢.mp3
        String album = displayName.substring(0, displayName.indexOf("."));//生命河灵粮堂 - 全然为祢

        String name;
        if(album.contains("-")){
            name = album.substring(album.lastIndexOf("-")+1).trim();//全然为祢
        }else{
            name = album;
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "AudioItem{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
