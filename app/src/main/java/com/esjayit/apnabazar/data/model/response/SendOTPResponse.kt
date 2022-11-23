package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SendOTPResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val data: OTPData = OTPData(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)
data class OTPData (

    @field:SerializedName("otpid")
    var otpId: String = "",

    @field:SerializedName("otpcount")
    var otpCount: String = "",

    var otpCounInt: Int = 6
) : Serializable