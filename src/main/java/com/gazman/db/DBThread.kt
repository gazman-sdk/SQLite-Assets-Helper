package com.gazman.db

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
object DBThread {
    var handler = Handler(HandlerThread("db_thread").looper)
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    fun execute(runnable: Runnable) {
        if (isOnDbThread) {
            runnable.run()
        } else {
            handler.post(runnable)
        }
    }

    fun synchronize() {
        lock.withLock {
            execute {
                lock.withLock {
                    condition.signalAll()
                }
            }
            condition.await()
        }
    }

    private val isOnDbThread: Boolean
        get() = handler.looper == Looper.myLooper()
}