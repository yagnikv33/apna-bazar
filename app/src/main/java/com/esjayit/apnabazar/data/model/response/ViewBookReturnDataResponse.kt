package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class ViewBookReturnDataResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Extra(
	val any: Any? = null
)

data class JsonMemberReturn(

	@field:SerializedName("retutranlist")
	val retutranlist: List<RetutranlistItem?>? = null,

	@field:SerializedName("approvestatus")
	val approvestatus: String? = null,

	@field:SerializedName("billamount")
	val billamount: String? = null,

	@field:SerializedName("approvecode")
	val approvecode: String? = null,

	@field:SerializedName("billdate")
	val billdate: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("billno")
	val billno: String? = null
)

data class Data(

	@field:SerializedName("return")
	val jsonMemberReturn: JsonMemberReturn? = null
)

data class RetutranlistItem(

	@field:SerializedName("tranid")
	val tranid: String? = null,

	@field:SerializedName("standard")
	val standard: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("maxretu")
	val maxretu: String? = null,

	@field:SerializedName("approvestatus")
	val approvestatus: String? = null,

	@field:SerializedName("subname")
	val subname: String? = null,

	@field:SerializedName("subcode")
	val subcode: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null,

	@field:SerializedName("thock")
	val thock: String? = null,

	@field:SerializedName("itemid")
	val itemid: String? = null,

	@field:SerializedName("buyqty")
	val buyqty: String? = null,

	@field:SerializedName("rate")
	val rate: String? = null,

	@field:SerializedName("approvedate")
	val approvedate: String? = null,

	@field:SerializedName("retuqty")
	val retuqty: String? = null
)
