package com.gazman.db;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public final class DBThread {

    public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
}
