package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class UserProfileDetailResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val userData: UData? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DetailModel(

	@field:SerializedName("ucountry")
	val ucountry: String? = null,

	@field:SerializedName("uname")
	val uname: String? = null,

	@field:SerializedName("panno")
	val panno: String? = null,

	@field:SerializedName("gstno")
	val gstno: String? = null,

	@field:SerializedName("uphone2")
	val uphone2: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("ustate")
	val ustate: String? = null,

	@field:SerializedName("uphone1")
	val uphone1: String? = null,

	@field:SerializedName("uaddress")
	val uaddress: String? = null,

	@field:SerializedName("uemail")
	val uemail: String? = null,

	@field:SerializedName("ucity")
	val ucity: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
data class UData(

	@field:SerializedName("userdata")
	val detail: DetailModel? = null
)
