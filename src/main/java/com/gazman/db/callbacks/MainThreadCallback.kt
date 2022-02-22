package com.gazman.db.callbacks

import android.os.Handler
import android.os.Looper

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
abstract class MainThreadCallback<T> protected constructor() : DataBaseDataCallback<T>(HANDLER) {
    companion object {
        val HANDLER = Handler(Looper.getMainLooper())
    }
}