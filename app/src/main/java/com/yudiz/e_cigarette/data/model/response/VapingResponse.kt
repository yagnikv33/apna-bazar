package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class VapingResponse(

    @field:SerializedName("data")
    val data: VapingData = VapingData(),

    @field:SerializedName("meta")
    val meta: VapingMeta = VapingMeta()
)

data class VapingData(

    @field:SerializedName("buttons")
    val buttons: List<ButtonsItem>? = null,

    @field:SerializedName("brands")
    val brands: List<BrandsItem>? = null
)

data class VapingBrandsItem(

    @field:SerializedName("logo")
    val logo: String = "",

    @field:SerializedName("id")
    val id: String = ""
)

data class VapingButtonsItem(

    @field:SerializedName("background_color")
    val backgroundColor: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("icon")
    val icon: String = "",

    @field:SerializedName("id")
    val id: String = ""
)

data class VapingMeta(

    @field:SerializedName("api")
    val api: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("url")
    val url: String = ""
)
