package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("access_token")
    val accessToken: String = "",

    @field:SerializedName("token_type")
    val tokenType: String = "",

    @field:SerializedName("expires_in")
    val expiresIn: String = "",

    @field:SerializedName("userid")
    val userId: String = "",

    @field:SerializedName("installid")
    val installId: String = "",

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)