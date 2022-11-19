package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class StandardResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: StandardData? = null,

	@field:SerializedName("extra")
	val extra: StandardExtra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class StandardExtra(
	val any: Any? = null
)

data class StandardData(

	@field:SerializedName("stdlist")
	val stdlist: List<StdlistItem?>? = null
)

data class StdlistItem(

	@field:SerializedName("std")
	val std: String? = null
)
