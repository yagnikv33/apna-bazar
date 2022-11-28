package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class SingleItemResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: ReturnSingleData? = null,

	@field:SerializedName("extra")
	val extra: ReturnSingleExtra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Returnitems(

	@field:SerializedName("standard")
	val standard: String? = null,

	@field:SerializedName("thock")
	val thock: String? = null,

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

	@field:SerializedName("medium")
	val medium: String? = null
)

data class ReturnSingleExtra(
	val any: Any? = null
)

data class ReturnSingleData(

	@field:SerializedName("returnitems")
	val returnitems: ReturnitemsSingle? = null
)
