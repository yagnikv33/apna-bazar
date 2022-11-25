package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class EditDemandDataResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: EditDemandData? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)
data class EditDemandData(

	@field:SerializedName("demand")
	val demand: Demand? = null
)
