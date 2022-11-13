package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class CheckUpdateResponse(

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("userdata")
    val userData: UserData = UserData(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)
data class UserData(

    @field:SerializedName("isUpdateAvailable")
    val isUpdateAvailable: String = "",

    @field:SerializedName("forceUpdate")
    val isForceUpdate: String = ""
)