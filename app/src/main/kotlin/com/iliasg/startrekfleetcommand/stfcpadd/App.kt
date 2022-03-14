package com.iliasg.startrekfleetcommand.stfcpadd

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {

                // Add a global tag for filtering purposes.
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, "Timber - $tag", message, t)
                }

                // Add method name to the logs.
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "%s:%s",
                        element.methodName,
                        super.createStackElementTag(element)
                    )
                }
            })
        }
    }
}