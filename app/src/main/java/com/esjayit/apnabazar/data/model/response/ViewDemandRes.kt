package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class ViewDemandRes(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: ViewDemandData? = null,

	@field:SerializedName("extra")
	val extra: ViewDemandExtra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ViewDemandExtra(
	val any: Any? = null
)

data class ViewDemandData(

	@field:SerializedName("demand")
	val demand: ViewDemandDemand? = null
)

data class ViewDemandDemand(

	@field:SerializedName("discountamt")
	val discountamt: String? = null,

	@field:SerializedName("partyname")
	val partyname: String? = null,

	@field:SerializedName("demanddate")
	val demanddate: String? = null,

	@field:SerializedName("itemslist")
	val itemslist: List<ViewDemandItemslistItem?>? = null,

	@field:SerializedName("viewdemanddate")
	val viewdemanddate: String? = null,

	@field:SerializedName("demandid")
	val demandid: String? = null,

	@field:SerializedName("totalamt")
	val totalamt: String? = null,

	@field:SerializedName("billdate")
	val billdate: String? = null,

	@field:SerializedName("grandtotal")
	val grandtotal: String? = null,

	@field:SerializedName("demandno")
	val demandno: String? = null,

	@field:SerializedName("viewbilldate")
	val viewbilldate: String? = null,

	@field:SerializedName("roundoff")
	val roundoff: String? = null,

	@field:SerializedName("billno")
	val billno: String? = null
)

data class ViewDemandItemslistItem(

	@field:SerializedName("thock")
	val thock: String? = null,

	@field:SerializedName("itemid")
	val itemid: String? = null,

	@field:SerializedName("std")
	val std: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("bunchqty")
	val bunchqty: String? = null,

	@field:SerializedName("rate")
	val rate: String? = null,

	@field:SerializedName("subname")
	val subname: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("rank")
	val rank: String? = null,

	@field:SerializedName("subcode")
	val subcode: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null,

	var isTextVisible:Boolean? = null
)
