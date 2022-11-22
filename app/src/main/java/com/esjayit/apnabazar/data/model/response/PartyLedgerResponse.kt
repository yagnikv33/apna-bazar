package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class PartyLedgerResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: LedgerData? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)
data class LedgerData(

	@field:SerializedName("ledger")
	val ledger: List<LedgerItem?>? = null
)
data class LedgerItem(

	@field:SerializedName("balance")
	val balance: String? = null,

	@field:SerializedName("narration")
	val narration: String? = null,

	@field:SerializedName("trandate")
	val trandate: String? = null,

	@field:SerializedName("credit")
	val credit: String? = null,

	@field:SerializedName("debit")
	val debit: String? = null
)
