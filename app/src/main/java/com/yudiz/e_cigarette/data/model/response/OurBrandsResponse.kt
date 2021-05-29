package com.yudiz.e_cigarette.data.model.response
import com.google.gson.annotations.SerializedName

data class OurBrandsResponse(

    @SerializedName("data")
    val data: List<BrandListData>? = null,

    @SerializedName("meta")
    val meta: BrandListMeta = BrandListMeta()
)

data class BrandListData(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("logo")
    val logo: String = ""
)

data class BrandListMeta(
    @SerializedName("api")
    val api: String = "",
    @SerializedName("count")
    val count: Int = -1,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("url")
    val url: String = ""
)
