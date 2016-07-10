package com.gazman.db.callbacks;

import android.os.Handler;

import java.util.concurrent.ExecutorService;

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
public abstract class DataBaseDataCallback<T> {

    private final Handler handler;
    private final ExecutorService executorService;

    protected DataBaseDataCallback(ExecutorService executorService) {
        this.executorService = executorService;
        handler = null;
    }

    protected DataBaseDataCallback(Handler handler) {
        this.handler = handler;
        executorService = null;
    }

    public void setResponse(final T responseData) {
        if(executorService != null){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    onResponse(responseData);
                }
            });
        }
        else{
            assert handler != null;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onResponse(responseData);
                }
            });
        }
    }

    protected abstract void onResponse(T responseData);
}
