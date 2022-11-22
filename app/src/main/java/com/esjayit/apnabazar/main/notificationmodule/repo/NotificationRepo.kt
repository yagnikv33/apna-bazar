package com.esjayit.apnabazar.main.notificationmodule.repo
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.api.service.NotificationApiModule
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo
import com.google.gson.JsonObject

class NotificationRepo(private val apiCall: NotificationApiModule) : BaseRepo() {

    suspend fun getNotificationList(
        userId: String,
        installId: String,
               onError: (ApiResult<Any>) -> Unit
    ): NotificationListResponse? {
        return with(apiCall(executable = { apiCall.getNotificationList(
            userId = userId,
            installId = installId
        ) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun readNotification(
        inboxId: String,
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): ReadNotificationResponse? {
        return with(apiCall(executable = { apiCall.readNotification(
            inboxId = inboxId,
            userId = userId,
            installId = installId
        ) })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}