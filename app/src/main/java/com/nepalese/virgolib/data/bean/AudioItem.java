package com.nepalese.virgolib.data.bean;

import com.nepalese.virgosdk.Beans.BaseBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2022/3/21.
 * Usage: 简单音频信息类
 */
@Entity
public class AudioItem extends BaseBean {
    @Id(autoincrement = true)
    private Long id;
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

    @Generated(hash = 1886452029)
    public AudioItem(Long id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    @Generated(hash = 1683268244)
    public AudioItem() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
