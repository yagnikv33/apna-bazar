package com.esjayit.apnabazar

import androidx.multidex.MultiDexApplication
import com.esjayit.apnabazar.instantiation.KoinModule
import com.onesignal.OneSignal
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initTasks()
    }

    private fun initTasks() {

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    KoinModule.utilModule,
                    KoinModule.vmModule,
                    KoinModule.apiModule,
                    KoinModule.repoModule
                )
            )
        }

        //For Setup OneSignal (Notification)
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(AppConstants.App.ONESIGNAL_APP_ID)
    }
}
