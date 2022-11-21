package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class GetReturnLisitngResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: ReturnListData? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RetunlistItem(

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

data class ReturnListData(

	@field:SerializedName("retunlist")
	val retunlist: List<RetunlistItem?>? = null
)
