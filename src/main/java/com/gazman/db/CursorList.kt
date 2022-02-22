package com.gazman.db

import android.database.Cursor
import android.text.TextUtils

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
abstract class CursorList<E>(cursor: Cursor) : ArrayList<E>() {
    protected abstract fun initColumnIndexes(cursor: Cursor?)
    protected abstract fun processRaw(cursor: Cursor?): E
    override fun toString(): String {
        return TextUtils.join(",", this)
    }

    init {
        if (cursor.moveToFirst()) {
            initColumnIndexes(cursor)
            do {
                add(processRaw(cursor))
            } while (cursor.moveToNext())
        }
    }
}