package com.gazman.db.callbacks;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public abstract class MainThreadCallback<T> extends DataBaseDataCallback<T> {
    static final Handler HANDLER = new Handler(Looper.getMainLooper());

    protected MainThreadCallback() {
        super(HANDLER);
    }
}
