package com.gazman.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public final class DBThread {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static volatile Thread thread;

    static {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                DBThread.thread = Thread.currentThread();
            }
        });
    }

    public static void execute(Runnable runnable) {
        while (thread == null){
            Thread.yield();
        }

        if (thread == Thread.currentThread()) {
            runnable.run();
        } else {
            EXECUTOR.execute(runnable);
        }
    }
}
