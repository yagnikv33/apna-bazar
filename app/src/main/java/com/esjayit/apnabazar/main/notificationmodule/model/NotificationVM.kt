package com.esjayit.apnabazar.main.notificationmodule.model

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.BaseVM
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.notificationmodule.repo.NotificationRepo
import com.google.gson.JsonObject
import java.io.File


class NotificationVM(private val repo: NotificationRepo) : BaseVM() {

    private val progressBar = MutableLiveData(false)
    //For Splash Screen Data
    var notiListData = MutableLiveData(NotificationListResponse())
    var readNotiData = MutableLiveData(ReadNotificationResponse())
    var notificationListData = mutableListOf<NotificationlistItem?>()


    //For Notification lIst
    fun getNotificationList( userId: String,
                             installId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.getNotificationList(
                userId = userId,
                installId = installId,
                onApiError
            ).let {
                notiListData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }

    //For Read Notification
    fun readNotification(inboxId: String, userId: String,
                             installId: String) {
        scope {
            progressBar.postValue(true)
            state.emit(ApiRenderState.Loading)
            repo.readNotification(
                inboxId = inboxId,
                userId = userId,
                installId = installId,
                onApiError
            ).let {
                readNotiData.postValue(it)
                state.emit(ApiRenderState.ApiSuccess(it))
                progressBar.postValue(false)
            }
        }
    }
}