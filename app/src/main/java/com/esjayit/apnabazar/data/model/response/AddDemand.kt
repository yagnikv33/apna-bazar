package com.esjayit.apnabazar.data.model.response

import com.esjayit.BuildConfig
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddDemand(
    @SerializedName("demanddate")
    private var demanddate: String? = null,
    @SerializedName("userid")
    private var userid: String? = null,
    @SerializedName("totalamt")
    private var totalamt: String? = null,
    @SerializedName("itemslist")
    private var itemslist: List<AddDemandForAPINew>? = null,
    @SerializedName("installid")
    private var installid: String? = null,
    @SerializedName("packagename")
    private var packagename: String? = BuildConfig.APPLICATION_ID,
    @SerializedName("versioncode")
    private var versioncode: String? = BuildConfig.VERSION_CODE.toString(),
    @SerializedName("message") var message: String? =null,
) : Serializable

