package com.gazman.db

import android.content.Context
import io.requery.android.database.DatabaseErrorHandler
import io.requery.android.database.sqlite.SQLiteDatabase

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
class DataBase {
    private var helper: DataBaseHelper? = null
    private fun init(helper: DataBaseHelper) {
        this.helper = helper
    }

    fun getReadableDatabase(queryCallback: DataBaseQueryCallback) {
        DBThread.execute { queryCallback.onQuery(DbProxy(helper!!.readableDatabase)) }
    }

    fun getWritableDatabase(queryCallback: DataBaseQueryCallback) {
        DBThread.execute { queryCallback.onQuery(DbProxy(helper!!.writableDatabase)) }
    }

    fun makeTransaction(queryCallback: DataBaseQueryCallback) {
        DBThread.execute {
            val db = helper!!.writableDatabase
            try {
                db.beginTransaction()
                queryCallback.onQuery(DbProxy(db))
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                ErrorAnalytics.logException(e)
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
        }
    }

    fun close() {
        DBThread.execute { helper!!.close() }
    }

    class Builder
    /**
     * Creates new database with default assetsPath: "databases"
     */(private val context: Context, private val dataBaseName: String) {
        private var assetsPath = "databases"
        private var cursorFactory: SQLiteDatabase.CursorFactory? = null
        private var databaseErrorHandler: DatabaseErrorHandler? = null
        private var version = 1
        private var upgradeCallback: UpgradeCallback? = null
        private var buildCallback: Runnable? = null

        fun setAssetsPath(assetsPath: String): Builder {
            this.assetsPath = assetsPath
            return this
        }

        fun setBuildCallback(buildCallback: Runnable?): Builder {
            this.buildCallback = buildCallback
            return this
        }

        fun setCursorFactory(cursorFactory: SQLiteDatabase.CursorFactory?): Builder {
            this.cursorFactory = cursorFactory
            return this
        }

        fun setDatabaseErrorHandler(databaseErrorHandler: DatabaseErrorHandler?): Builder {
            this.databaseErrorHandler = databaseErrorHandler
            return this
        }

        fun setVersion(version: Int): Builder {
            this.version = version
            return this
        }

        fun setUpgradeCallback(upgradeCallback: UpgradeCallback): Builder {
            this.upgradeCallback = upgradeCallback
            return this
        }

        fun build(): DataBase {
            val dataBase = DataBase()
            DBThread.execute {
                val helper = DataBaseHelper(
                    context,
                    dataBaseName,
                    version,
                    cursorFactory,
                    databaseErrorHandler
                )
                dataBase.init(helper)
                helper.setAssetsPath(assetsPath)
                helper.upgradeCallback = upgradeCallback
                helper.buildCallback = buildCallback
                helper.load()
            }
            return dataBase
        }
    }
}