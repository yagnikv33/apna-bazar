package com.yudiz.e_cigarette.data.model.response


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.google.gson.annotations.SerializedName

data class PgVgResponse(
    @SerializedName("data")
    val data: PgVgData = PgVgData(),

    @SerializedName("meta")
    val meta: PgVgMeta = PgVgMeta()
)

data class PgVgData(

    @SerializedName("pg_vg")
    val pgVg: PgVg = PgVg(),

    @SerializedName("advertisements")
    val advertisements: PgVgAdvertisements = PgVgAdvertisements()
)

data class PgVg(
    @SerializedName("description")
    val description: String = "",

    @SerializedName("id")
    val id: String = ""
)

data class PgVgAdvertisements(
    @SerializedName("ads")
    val ads: String = "",

    @SerializedName("id")
    val id: String = "",

    @SerializedName("type")
    val type: String = ""
)


data class PgVgMeta(
    @SerializedName("api")
    val api: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("url")
    val url: String = ""
)