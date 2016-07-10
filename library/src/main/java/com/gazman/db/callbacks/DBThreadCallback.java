package com.gazman.db.callbacks;

import com.gazman.db.DBThread;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public abstract class DBThreadCallback<T> extends DataBaseDataCallback<T> {

    protected DBThreadCallback() {
        super(DBThread.EXECUTOR);
    }
}
