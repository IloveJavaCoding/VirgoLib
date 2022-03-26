package com.nepalese.virgolib.data.dbhelper;

import com.nepalese.virgolib.data.bean.AudioItem;
import com.nepalese.virgolib.data.db.AudioItemDao;
import com.nepalese.virgolib.data.db.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2022/3/26.
 * Usage: 管理数据库dao的增删查改操作
 */

public class DBManager {
    private final DaoSession daoSession;

    public DBManager(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    //////////////获取各种dao实例：私有////////////////////////////
    private AudioItemDao getAudioItemDao(){
        return daoSession.getAudioItemDao();
    }


    //////////////dao的增删查改操作：公开////////////////////////////
    public void addAudioItem(AudioItem item){
        getAudioItemDao().insertOrReplace(item);
    }

    public void addAudioItems(List<AudioItem> list){
        getAudioItemDao().insertInTx(list);
    }

    public void deleteAudioItem(AudioItem item){
        getAudioItemDao().delete(item);
    }

    public void deleteAudioItemById(Long id){
        getAudioItemDao().deleteByKey(id);
    }

    public void updateAudioItem(AudioItem item){
        getAudioItemDao().update(item);
    }

    public List<AudioItem> getAllAudioItem(){
        return getAudioItemDao().loadAll();
    }

    public AudioItem getAudioItemByName(String name){
        QueryBuilder<AudioItem> qb = getAudioItemDao().queryBuilder();
        qb.where(AudioItemDao.Properties.Name.eq(name));
        List<AudioItem> list = qb.build().list();
        return list==null? null : list.get(0);
    }
}
