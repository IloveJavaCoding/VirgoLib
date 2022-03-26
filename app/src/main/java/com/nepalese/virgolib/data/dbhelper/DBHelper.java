package com.nepalese.virgolib.data.dbhelper;

import android.content.Context;

import com.nepalese.virgolib.data.db.DaoMaster;
import com.nepalese.virgolib.data.db.DaoSession;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

/**
 * Created by Administrator on 2022/3/25.
 * Usage: 数据库管理器
 */

public class DBHelper {
    public static final String DATABASE_NAME = "VirgoLib.db";

    private static volatile DBHelper instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DBManager dbManager;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    public DBHelper(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context not null");
        }

        if (daoSession == null) {
            if (daoMaster == null) {
                MyDBOpenHelper helper = new MyDBOpenHelper(context, DATABASE_NAME);
                daoMaster = new DaoMaster(helper.getWritableDb());
            }
            daoSession = daoMaster.newSession();

            dbManager = new DBManager(daoSession);
        }
    }

    public DBManager getDbManager() {
        return dbManager;
    }
}
