package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class GetReturnItemListResponse(

    @field:SerializedName("statuscode")
    val statuscode: String? = null,

    @field:SerializedName("data")
    val data: ReturnItemsData? = null,

    @field:SerializedName("extra")
    val extra: Extra? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ReturnItemsData(

    @field:SerializedName("returnitems")
    val returnitems: List<ReturnitemsItem?>? = null
)

data class ReturnitemsItem(

    @field:SerializedName("itemid")
    val itemid: String? = null,

    @field:SerializedName("buyqty")
    val buyqty: String? = null,

    @field:SerializedName("rate")
    val rate: String? = null,

    @field:SerializedName("maxretu")
    val maxretu: String? = null,

    @field:SerializedName("subname")
    val subname: String? = null,

    @field:SerializedName("subcode")
    val subcode: String? = null,

    @field:SerializedName("retuqty")
    var retuqty: String? = null,

    var isTextVisible: Boolean = false
) {
    var returnItemResponse: ReturnitemsSingle? = null
}
