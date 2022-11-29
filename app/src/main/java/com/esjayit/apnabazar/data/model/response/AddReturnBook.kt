package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class AddReturnBook(

	@field:SerializedName("retutranlist")
	val retutranlist: List<AddRetutranlistItem?>? = null,

	@field:SerializedName("billamount")
	val billamount: String? = null,

	@field:SerializedName("billdate")
	val billdate: String? = null,

	@field:SerializedName("packagename")
	val packagename: String? = null,

	@field:SerializedName("versioncode")
	val versioncode: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("installid")
	val installid: String? = null
)

data class AddRetutranlistItem(

	@field:SerializedName("itemid")
	val itemid: String? = null,

	@field:SerializedName("buyqty")
	val buyqty: String? = null,

	@field:SerializedName("maxretu")
	val maxretu: String? = null,

	@field:SerializedName("rate")
	val rate: String? = null,

	@field:SerializedName("retuqty")
	val retuqty: String? = null
)
