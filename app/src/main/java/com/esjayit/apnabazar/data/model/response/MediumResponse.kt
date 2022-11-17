package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class MediumResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: MediumData? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Extra(
	val any: Any? = null
)

data class MediumlistItem(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class MediumData(

	@field:SerializedName("mediumlist")
	val mediumlist: List<MediumlistItem?>? = null
)
