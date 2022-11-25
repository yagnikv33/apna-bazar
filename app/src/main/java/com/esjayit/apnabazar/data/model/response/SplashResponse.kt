package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SplashResponse(

    @field:SerializedName("statuscode")
    var statusCode: String = "",

    @field:SerializedName("message")
    var message: String = "",

    @field:SerializedName("data")
    val data: Data = Data(),

    @field:SerializedName("meta")
    val meta: Meta = Meta()
)

data class Meta(

    @field:SerializedName("api")
    val api: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("url")
    val url: String = ""
)

//data class Data(
//    @field:SerializedName("background_color")
//    val backgroundColor: String = "",
//
//    @field:SerializedName("video")
//    val video: String = "",
//
//    @field:SerializedName("title")
//    val title: String = ""
//) : Serializable