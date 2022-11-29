package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class AddDemandForAPINew(
    @SerializedName("itemid")
    private var itemid: String? = null,
    @SerializedName("qty")
    private var qty: String? = null,
    @SerializedName("rate")
    private var rate: String? = null,
    @SerializedName("amount")
    private var amount: String? = null,
    @SerializedName("bunchqty")
    private var bunchqty: String? = null,
)