package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

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