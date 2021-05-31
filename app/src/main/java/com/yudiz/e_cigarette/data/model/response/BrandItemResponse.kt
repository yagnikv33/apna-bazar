package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class BrandItemResponse(

    @field:SerializedName("data")
    val data: BrandData? = BrandData(),

    @field:SerializedName("meta")
    val meta: BrandMeta? = BrandMeta()
)

data class BrandData(

    @field:SerializedName("ads")
    val ads: String = "",

    @field: SerializedName("description")
    val description: String = "",

    @field:SerializedName("hardware")
    val hardware: List<JuiceAndHardware>? = null,

    @field:SerializedName("juices")
    val juices: List<JuiceAndHardware>? = null,

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("logo")
    val logo: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("type")
    val type: String = ""
)

data class JuiceAndHardware(

    @field:SerializedName("background_color")
    val background_color: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("label")
    val label: String = "",

    @field:SerializedName("label_name")
    val label_name: String = ""
)


data class Juice(

    @field:SerializedName("background_color")
    val background_color: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("label")
    val label: String = "",

    @field:SerializedName("label_name")
    val label_name: String = ""

)

data class Hardware(

    @field:SerializedName("background_color")
    val background_color: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("image")
    val image: String = "",

    @field:SerializedName("label")
    val label: String = ""

)

data class BrandMeta(

    @field:SerializedName("api")
    val api: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("url")
    val url: String = ""
)