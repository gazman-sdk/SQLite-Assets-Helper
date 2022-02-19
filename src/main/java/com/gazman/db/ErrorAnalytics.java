package com.gazman.db;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

class ErrorAnalytics {
    public static void logException(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
        FirebaseCrashlytics.getInstance().recordException(e);
    }
}
