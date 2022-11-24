package com.gazman.db.callbacks

import android.os.Handler
import java.util.concurrent.ExecutorService

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
abstract class DataBaseDataCallback<T> {
    private val handler: Handler?
    private val executorService: ExecutorService?

    protected constructor(executorService: ExecutorService?) {
        this.executorService = executorService
        handler = null
    }

    protected constructor(handler: Handler?) {
        this.handler = handler
        executorService = null
    }

    fun sendResponse(responseData: T) {
        if (executorService != null) {
            executorService.execute(Runnable { onResponse(responseData) })
        } else {
            assert(handler != null)
            handler!!.post { onResponse(responseData) }
        }
    }

    protected abstract fun onResponse(responseData: T)
}