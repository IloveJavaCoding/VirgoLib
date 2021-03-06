package com.nepalese.virgolib.data.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.nepalese.virgolib.data.bean.AudioItem;

import com.nepalese.virgolib.data.db.AudioItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig audioItemDaoConfig;

    private final AudioItemDao audioItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        audioItemDaoConfig = daoConfigMap.get(AudioItemDao.class).clone();
        audioItemDaoConfig.initIdentityScope(type);

        audioItemDao = new AudioItemDao(audioItemDaoConfig, this);

        registerDao(AudioItem.class, audioItemDao);
    }
    
    public void clear() {
        audioItemDaoConfig.clearIdentityScope();
    }

    public AudioItemDao getAudioItemDao() {
        return audioItemDao;
    }

}
