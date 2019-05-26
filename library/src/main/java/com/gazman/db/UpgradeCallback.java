package com.gazman.db;

import io.requery.android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public interface UpgradeCallback {
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
