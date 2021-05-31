package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class BrandsItem(
    @field:SerializedName("logo")
    val logo: String? = "",

    @field:SerializedName("id")
    val id: String = ""
)
