package com.gazman.db;

import com.crashlytics.android.Crashlytics;

class ErrorAnalytics {
    public static void logException(Exception e) {
        if(BuildConfig.DEBUG) {
            e.printStackTrace();
        }
        Crashlytics.logException(e);
    }
}
