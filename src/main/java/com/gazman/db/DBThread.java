package com.gazman.db;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public final class DBThread {

    static Handler handler;

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler(Looper.myLooper());
                Looper.loop();
            }
        }).start();
    }

    public static void execute(Runnable runnable) {
        while (handler == null) {
            Thread.yield();
        }

        if (isOnDbThread()) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    public static void synchronize() {
        synchronized (DBThread.class) {
            execute(() -> {
                synchronized (DBThread.class) {
                    DBThread.class.notifyAll();
                }
            });
            try {
                DBThread.class.wait();
            } catch (InterruptedException e) {
                ErrorAnalytics.logException(e);
                e.printStackTrace();
            }
        }
    }

    public static boolean isOnDbThread() {
        return handler.getLooper() == Looper.myLooper();
    }
}
