package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class DemandListResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: DemandListData? = null,

	@field:SerializedName("extra")
	val extra: DemandListExtra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DemandListItem(

	@field:SerializedName("demanddate")
	val demanddate: String? = null,

	@field:SerializedName("demandcolorcode")
	val demandcolorcode: String? = null,

	@field:SerializedName("demandno")
	val demandno: String? = null,

	@field:SerializedName("demandstatus")
	val demandstatus: String? = null,

	@field:SerializedName("did")
	val did: String? = null
)

data class DemandListExtra(
	val any: Any? = null
)

data class DemandListData(

	@field:SerializedName("demandlist")
	val demandlist: List<DemandListItem?>? = null
)
