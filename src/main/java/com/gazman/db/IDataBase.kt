package com.gazman.db

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
interface IDataBase {
    fun getReadableDatabase(queryCallback: DataBaseQueryCallback)
    fun getWritableDatabase(queryCallback: DataBaseQueryCallback)

    fun makeTransaction(queryCallback: DataBaseQueryCallback)

    fun close()
}