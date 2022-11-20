package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class CheckUpdateResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val updateData: UpdateData = UpdateData(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)
data class UpdateData(

    @field:SerializedName("userdata")
    val updateDataModel: UpdateDataModel = UpdateDataModel()
)
data class UpdateDataModel(

    @field:SerializedName("isUpdateAvailable")
    val isUpdateAvailable: String = "",

    @field:SerializedName("forceUpdate")
    val isForceUpdate: String = ""
)