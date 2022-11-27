package com.esjayit.apnabazar

import android.R.attr.phoneNumber
import android.content.Intent
import androidx.multidex.MultiDexApplication
import com.esjayit.apnabazar.helper.util.PrefUtil
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.instantiation.KoinModule
import com.esjayit.apnabazar.onesignal.NotificationOpenedHandlerOneSignal
import com.onesignal.OneSignal
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : MultiDexApplication() {

    private val prefs by inject<PrefUtil>()

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

        initOneSignal()
    }

    private fun initOneSignal() {
        OneSignal.initWithContext(this.applicationContext)
        OneSignal.setAppId(AppConstants.App.ONESIGNAL_APP_ID)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)

        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandlerOneSignal(this))
        OneSignal.addSubscriptionObserver { state ->
            "One signal $state".logE()
            state?.let {
                if (!it.from.isSubscribed && it.to.isSubscribed) {
                    // get player ID
                    val oneSignalPlayerId = state.to.userId
                    if (prefs.playerId != oneSignalPlayerId) {
                        prefs.playerId = oneSignalPlayerId
                        // call api
                    }
                    "One signal $state".logE()
                }
            }
        }

        OneSignal.getDeviceState()?.userId?.let {
            prefs.playerId = it
        }
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    }
}
