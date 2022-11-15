package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class GetHomeDataResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val data: GetHomeData = GetHomeData(),

    @field:SerializedName("extra")
    val meta: Meta = Meta()
)
data class GetHomeData (

    @field:SerializedName("list")
    val list: List<ListItem?>? = null
)
data class ListItem (

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)