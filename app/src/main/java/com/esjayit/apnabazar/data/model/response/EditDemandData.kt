package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class EditDemandDataVal(

	@field:SerializedName("demandid")
	val demandid: String? = null,

	@field:SerializedName("totalamt")
	val totalamt: String? = null,

	@field:SerializedName("demanddate")
	val demanddate: String? = null,

	@field:SerializedName("itemslist")
	val itemslist: List<AddItemslistItem?>? = null,

	@field:SerializedName("packagename")
	val packagename: String? = null,

	@field:SerializedName("versioncode")
	val versioncode: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("installid")
	val installid: String? = null
)

data class AddItemslistItem(

	@field:SerializedName("tranid")
	val tranid: String? = null,

	@field:SerializedName("itemid")
	val itemid: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("bunchqty")
	val bunchqty: String? = null,

	@field:SerializedName("rate")
	val rate: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null
)
