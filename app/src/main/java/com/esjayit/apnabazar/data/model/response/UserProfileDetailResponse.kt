package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(

	@field:SerializedName("statuscode")
	val statusCode: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)