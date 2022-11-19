package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class HomeScreenListResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: GetHomeList? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)
data class GetHomeList (

	@field:SerializedName("list")
	val list: List<ListItem?>? = null
)

data class ListItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
