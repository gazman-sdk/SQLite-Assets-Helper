package com.gazman.db

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
fun interface UpgradeCallback {
    fun onUpgrade(db: DbProxy, oldVersion: Int, newVersion: Int)
}