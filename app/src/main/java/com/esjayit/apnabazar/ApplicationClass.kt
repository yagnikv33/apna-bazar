package com.esjayit.apnabazar

import android.app.Application
import com.esjayit.apnabazar.AppConstants.App.ONESIGNAL_APP_ID
import com.onesignal.OneSignal

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}