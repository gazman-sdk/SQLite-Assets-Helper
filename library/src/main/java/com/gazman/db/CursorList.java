package com.gazman.db;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public abstract class CursorList<E> extends ArrayList<E> {
    public CursorList(Cursor cursor) {
        if (cursor.moveToFirst()) {
            initColumnIndexes(cursor);
            do {
                add(processRaw(cursor));
            } while (cursor.moveToNext());
        }

    }

    protected abstract void initColumnIndexes(Cursor cursor);

    protected abstract E processRaw(Cursor cursor);

    @NonNull
    @Override
    public String toString() {
        return TextUtils.join(",", this);
    }
}
