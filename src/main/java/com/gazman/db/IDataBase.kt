package com.gazman.db

import android.content.Context
import io.requery.android.database.DatabaseErrorHandler
import io.requery.android.database.sqlite.SQLiteDatabase

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
interface IDataBase {
    fun getReadableDatabase(queryCallback: DataBaseQueryCallback)
    fun getWritableDatabase(queryCallback: DataBaseQueryCallback)

    fun makeTransaction(queryCallback: DataBaseQueryCallback)

    fun close()
}