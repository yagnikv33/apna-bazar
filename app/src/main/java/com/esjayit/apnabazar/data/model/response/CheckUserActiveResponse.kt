package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class CheckUserActiveResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val data: UserActiveData = UserActiveData(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)

data class UserActiveData(
    @field:SerializedName("isactive")
    val isActive: String = "",

    @field:SerializedName("ismultideviceallow")
    val isMultiDeviceAllow: String = "",
)