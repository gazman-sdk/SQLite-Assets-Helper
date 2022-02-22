package com.gazman.db

import com.gazman.db.callbacks.DataBaseDataCallback

/**
 * Created by Ilya Gazman on 7/9/2016.
 */
abstract class DBThreadCallback<T> protected constructor() :
    DataBaseDataCallback<T>(DBThread.handler)