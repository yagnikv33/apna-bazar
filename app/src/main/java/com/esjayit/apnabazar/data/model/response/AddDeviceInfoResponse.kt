package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class AddDeviceInfoResponse(

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)