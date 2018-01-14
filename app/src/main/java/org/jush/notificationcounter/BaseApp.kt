package org.jush.notificationcounter

import android.app.Application
import timber.log.Timber

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
