package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(

    var id: String? = "",

    @field:SerializedName("data")
    val data: PortalData? = PortalData(),

    @field:SerializedName("meta")
    val meta: Meta? = Meta()
)

data class PortalData(

    @field:SerializedName("buttons")
    val buttons: List<ButtonsItem>? = null,

    @field:SerializedName("brands")
    val brands: List<BrandsItem>? = null
)

data class ButtonsItem(

    @field:SerializedName("background_color")
    val backgroundColor: String? = "",

    @field:SerializedName("name")
    val name: String? = "",

    @field:SerializedName("icon")
    val icon: String? = "",

    @field:SerializedName("id")
    val id: String? = ""
)

data class BrandsItem(

    @field:SerializedName("logo")
    val logo: String? = "",

    @field:SerializedName("id")
    val id: String? = ""
)
