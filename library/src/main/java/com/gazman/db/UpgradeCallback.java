package com.gazman.db;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public interface UpgradeCallback {
    void onUpgrade(DbProxy db, int oldVersion, int newVersion);
}
