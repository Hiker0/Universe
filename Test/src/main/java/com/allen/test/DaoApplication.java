package com.allen.test;

import android.app.Application;

import com.allen.test.data.greendao.DaoMaster;
import com.allen.test.data.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Johnson on 2017-04-24.
 */

public class DaoApplication extends Application {
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
