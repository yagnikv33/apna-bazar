package com.esjayit.apnabazar.data.model.response

import com.google.gson.annotations.SerializedName

data class NotificationListResponse(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val data: NotificationData? = null,

	@field:SerializedName("extra")
	val extra: Extra? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class NotificationlistItem(

	@field:SerializedName("datetime")
	val datetime: String? = null,

	@field:SerializedName("isread")
	val isread: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("inboxid")
	val inboxid: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class NotificationData(

	@field:SerializedName("inboxlist")
	val inboxlist: List<NotificationlistItem?>? = null
)
