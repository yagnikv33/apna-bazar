package com.esjayit.apnabazar.api.service

import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_NOTIFICATION_LISTING
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.SET_NOTIFICATION_READ
import com.esjayit.apnabazar.data.model.response.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NotificationApiModule {

    @FormUrlEncoded
    @POST(GET_NOTIFICATION_LISTING)
    suspend fun getNotificationList(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId: String
    ): NotificationListResponse

    @FormUrlEncoded
    @POST(SET_NOTIFICATION_READ)
    suspend fun readNotification(
        @Field("userid") userId: String,
        @Field("inboxid") inboxId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId: String
    ): ReadNotificationResponse

}