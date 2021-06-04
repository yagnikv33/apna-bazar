package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class PersonaliseVapeResponse(

    @field:SerializedName("data")
    val data: PersonaliseVapeData = PersonaliseVapeData(),

    @field:SerializedName("meta")
    val meta: PersonaliseVape = PersonaliseVape()
)

data class PersonaliseVapeData(

    @field:SerializedName("advertisements")
    val advertisements: List<AdvertisementsItem>? = null,

    @field:SerializedName("questions")
    val questions: List<QuestionsItem>? = null
)

data class QuestionsItem(

    @field:SerializedName("question")
    val question: String = "",

    @field:SerializedName("background_color")
    val backgroundColor: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("slug")
    val slug: String = "",

    @field:SerializedName("options")
    val options: List<OptionsItem>? = null,
)

data class OptionsItem(

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("option")
    val option: String = ""
)

data class AdvertisementsItem(

    @field:SerializedName("ads")
    val ads: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("type")
    val type: String = "",

    @field:SerializedName("slug")
    val slug: String = ""
)

data class PersonaliseVape(

    @field:SerializedName("api")
    val api: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("url")
    val url: String = ""
)