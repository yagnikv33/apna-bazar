package com.skeletonkotlin.e_cigarette.background.service.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.skeletonkotlin.R
import com.skeletonkotlin.e_cigarette.Strings
import com.skeletonkotlin.e_cigarette.main.entrymodule.view.MainAct
import java.util.*

class FcmService : FirebaseMessagingService() {

    private val randomNumber: Int
        get() {
            return Date().time.toString().let {
                Integer.valueOf(it.substring(it.length - 5))
            }
        }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        try {
            val bundle = Bundle()
            for ((key, value) in remoteMessage.data)
                bundle.putString(key, value)

            var title: String? = getString(Strings.app_name)
            var body: String? = ""
            remoteMessage.data.let {
                if (it.isNotEmpty()) {
                    title = it.getOrElse("title", { title })
                    body = it.getOrElse("body", { "" })
                }
            }
            showNotification(bundle, title, body)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    private fun showNotification(bundle: Bundle, title: String?, body: String?) {
        val channelId = getString(Strings.app_name)
        val notificationId = randomNumber

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                it.createNotificationChannel(
                    NotificationChannel(
                        channelId, channelId,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )

            it.notify(
                notificationId, NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(ContextCompat.getColor(baseContext, R.color.colorAccent))
                    .setContentIntent(
                        PendingIntent.getActivity(
                            this,
                            notificationId,
                            Intent(this@FcmService, MainAct::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                                putExtras(bundle)
                            },
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    )
                    .setChannelId(channelId).build()
            )
        }
    }
}