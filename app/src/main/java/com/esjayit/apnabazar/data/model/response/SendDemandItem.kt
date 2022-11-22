package com.esjayit.apnabazar.data.model.response

import retrofit2.http.Field

data class SendDemandItem(
    @Field("itemid") val itemid: String,
    @Field("qty") val qty: String,
    @Field("rate") val rate: String,
    @Field("amount") val amount: String,
    @Field("bunchqty") val bunchqty: String
)