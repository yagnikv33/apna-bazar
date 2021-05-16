package com.skeletonkotlin.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("data")
	val data: Data? = Data(),

	@field:SerializedName("meta")
	val meta: Meta? = Meta()
)

data class BrandsItem(

	@field:SerializedName("logo")
	val logo: String? = "",

	@field:SerializedName("id")
	val id: String? = ""
)

data class ButtonsItem(

	@field:SerializedName("background_color")
	val backgroundColor: String? = "",

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("icon")
	val icon: String? = "",

	@field:SerializedName("id")
	val id: String? = ""
)
