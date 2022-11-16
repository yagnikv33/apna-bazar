package com.esjayit.apnabazar.onesignal

import android.content.Context
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal

class NotificationOpenedHandlerOneSignal(var context: Context) :
    OneSignal.OSNotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenedResult?) {
        result?.let {
            val actionType = result.action.type
            val data = result.notification.additionalData

            if (actionType == OSNotificationAction.ActionType.Opened && data != null) {

            }
        }
    }
}

// This fires when a notification is opened by tapping on it.

/*    override fun notificationOpened(result: OSNotificationOpenResult) {
//        val actionType = result.action.type
//        val data = result.notification.payload.additionalData
//        val customKey: String?
//
//        if (data != null) {
//            customKey = data.optString("customkey", null)
//            if (customKey != null)
//                Log.e("OneSignalExample", "customkey set with value: $customKey")
//        }
//
//        if (actionType == OSNotificationAction.ActionType.ActionTaken)
//            Log.e("OneSignalExample", "Button pressed with id: " + result.action.actionID)
//
//        // The following can be used to open an Activity of your choice.
//        // Replace - getApplicationContext() - with any Android Context.
//        val intent = Intent(context, InitMainAct::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(intent)
        val actionType = result.action.type
        val data = result.notification.payload.additionalData

        if (actionType == OSNotificationAction.ActionType.Opened && data != null && data.has("topic")) {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleData.NOTIFICATION, data.toString())
            bundle.putString(AppConstants.BundleData.IN_APP_NOTIFICATION_CLICKED, data.toString())

            context.startActivity(Intent(context, InitMainAct::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtras(bundle))
        }

    }*/