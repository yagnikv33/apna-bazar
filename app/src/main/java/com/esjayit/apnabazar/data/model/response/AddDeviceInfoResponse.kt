package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class AddDeviceInfoResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)

data class ReadNotificationResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)