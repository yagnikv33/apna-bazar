package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class BrandDetailResponse(

    @field:SerializedName("data")
    val data: BrandData? = BrandData(),

    @field:SerializedName("meta")
    val meta: Meta? = Meta()
)

data class BrandData(

    @field:SerializedName("juices")
    val juices: List<JuicesItem?>? = null,

    @field:SerializedName("ads")
    val ads: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("hardware")
    val hardware: List<HardwareItem?>? = null
)

data class JuicesItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("background_color")
    val backgroundColor: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("label_name")
    val labelName: String? = null
)

data class HardwareItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("background_color")
    val backgroundColor: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("label")
    val label: String? = null
)
