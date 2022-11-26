package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class GetReturnResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: ReturnData? = null,

	@field:SerializedName("extra")
	val extra: ReturnExtra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ReturnExtra(
	val any: Any? = null
)

data class ReturnData(

	@field:SerializedName("returnitems")
	val returnitems: List<ReturnReturnitemsItem?>? = null
)

data class ReturnReturnitemsItem(

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
	val retuqty: String? = null,

	var isTextVisible: Boolean = false
)
