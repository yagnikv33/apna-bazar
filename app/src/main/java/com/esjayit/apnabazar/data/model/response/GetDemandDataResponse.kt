package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class GetDemandDataResponse(

    @field:SerializedName("statuscode")
    val statuscode: String? = null,

    @field:SerializedName("data")
    val data: ItemData? = ItemData(),

    @field:SerializedName("extra")
    val extra: ItemExtra? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ItemData(

    @field:SerializedName("itemlist")
    val itemlist: List<ItemlistItem?>? = null
)

data class ItemlistItem(

    @field:SerializedName("thock")
    val thock: String? = null,

    @field:SerializedName("itemid")
    val itemid: String? = null,

    @field:SerializedName("subname")
    val subname: String? = null,

    @field:SerializedName("standard")
    val standard: String? = null,

    @field:SerializedName("itemrate")
    val itemrate: String? = null,

    var qty:String = "",

    var mediumItem: String? = null,

    var isTextVisible: Boolean = false
)

data class ItemExtra(
    val any: Any? = null
)


