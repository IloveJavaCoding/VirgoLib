package com.nepalese.virgolib.data.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nepalese.virgolib.data.db.AudioItemDao;
import com.nepalese.virgolib.data.db.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2022/3/26.
 * Usage: 继承DaoMaster.OpenHelper，， 重写升级数据库功能
 */

public class MyDBOpenHelper extends DaoMaster.OpenHelper {

    public MyDBOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        //自定义数据库版本变化后如何处理
        try {
            if (newVersion > oldVersion) {
                //升级： 合并原有数据结构
                MigrationHelper.getInstance().migrate(db, AudioItemDao.class);
            } else if (newVersion < oldVersion) {
                //降级: 清空重建
                DaoMaster.dropAllTables(db, true);
                onCreate(db);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
