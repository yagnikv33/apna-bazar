package com.yudiz.e_cigarette.data.model.response

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.google.gson.annotations.SerializedName

data class KnowFactsResponse(


    @field:SerializedName("data")
    val data: FactsData? = FactsData(),

    @field:SerializedName("meta")
    val meta: FactsMeta? = FactsMeta()
)

data class FactsData(

    @field:SerializedName("advertisements")
    val advertisements: FactsAdvertisements = FactsAdvertisements(),

    @field:SerializedName("know_the_facts")
    var knowTheFacts: List<KnowTheFactsItem>? = null
)

data class KnowTheFactsItem(

    @field:SerializedName("description")
    var description: String = "",

    @field:SerializedName("id")
    val id: String = ""
) {
    val title
        @RequiresApi(Build.VERSION_CODES.N)
        get() = HtmlCompat.fromHtml(
            description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
}

data class FactsAdvertisements(

    @field:SerializedName("ads")
    val ads: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("type")
    val type: String = ""
)

data class FactsMeta(

    @field:SerializedName("api")
    val api: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("url")
    val url: String = ""
)