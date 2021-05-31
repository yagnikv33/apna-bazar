package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(

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