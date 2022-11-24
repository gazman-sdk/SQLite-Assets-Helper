package com.gazman.db

import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal object ErrorAnalytics {
    fun logException(e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}