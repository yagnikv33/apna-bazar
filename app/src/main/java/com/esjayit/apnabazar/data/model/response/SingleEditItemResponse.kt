package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class SingleEditItemResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: EditData? = null,

	@field:SerializedName("extra")
	val extra: EditExtra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class EditData(

	@field:SerializedName("itemdetail")
	val itemdetail: Itemdetail? = null
)

data class Itemdetail(

	@field:SerializedName("standard")
	val standard: String? = null,

	@field:SerializedName("thock")
	val thock: String? = null,

	@field:SerializedName("itemid")
	val itemid: String? = null,

	@field:SerializedName("gsebcode")
	val gsebcode: String? = null,

	@field:SerializedName("subname")
	val subname: String? = null,

	@field:SerializedName("subcode")
	val subcode: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null,

	@field:SerializedName("itemrate")
	val itemrate: String? = null
)

data class EditExtra(
	val any: Any? = null
)
