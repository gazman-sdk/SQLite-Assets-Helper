package com.gazman.db

import android.content.Context
import io.requery.android.database.DatabaseErrorHandler
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
internal class DataBaseHelper(
    private val context: Context,
    private val dataBaseName: String,
    version: Int,
    factory: SQLiteDatabase.CursorFactory?,
    error: DatabaseErrorHandler?
) : SQLiteOpenHelper(
    context, dataBaseName, factory, version, error
) {

    var upgradeCallback: UpgradeCallback? = null
    var buildCallback: Runnable? = null

    private var dataBasePath = "${context.applicationInfo.dataDir}/databases/"
    private lateinit var assetsPath: String

    fun load() {

        val isDataBaseExist = File(dataBasePath + dataBaseName).exists()
        if (!isDataBaseExist) {
            createDataBase()
            buildCallback?.run()
        }
        invokeUpgrade()
    }

    private fun invokeUpgrade() {
        readableDatabase
    }

    fun setAssetsPath(assetsPath: String) {
        var path = assetsPath
        if (path.startsWith("/")) {
            path = path.substring(1)
        }
        if (!path.endsWith("/")) {
            path += "/"
        }
        this.assetsPath = path
    }

    private fun createDataBase() {
        readableDatabase
        close()
        copyDataBase()
    }

    private fun copyDataBase() {
        val assetsPath = "$assetsPath$dataBaseName.db"
        val outFileName = dataBasePath + dataBaseName
        try {
            context.assets.open(assetsPath).use { input ->
                FileOutputStream(outFileName).use { output ->
                    val buffer = ByteArray(1024)
                    var length = input.read(buffer)
                    while (length != -1) {
                        if (length > 0) {
                            output.write(buffer, 0, length)
                        }
                        length = input.read(buffer)
                    }
                }
            }
        } catch (e: IOException) {
            ErrorAnalytics.logException(e)
            throw Error("Database not found in assets $outFileName")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (upgradeCallback != null) {
            DBThread.execute { upgradeCallback!!.onUpgrade(DbProxy(db), oldVersion, newVersion) }
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (BuildConfig.DEBUG) {
            return
        }
        super.onDowngrade(db, oldVersion, newVersion)
    }
}