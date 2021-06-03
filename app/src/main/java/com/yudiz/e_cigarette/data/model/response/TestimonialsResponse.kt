package com.yudiz.e_cigarette.data.model.response

import com.google.gson.annotations.SerializedName

data class TestimonialsResponse(

    @field:SerializedName("data")
    val data: TestimonialsData? = TestimonialsData(),

    @field:SerializedName("meta")
    val meta: TestimonialsMeta? = TestimonialsMeta()
)

data class TestimonialsData(

    @field:SerializedName("testimonials")
    val testimonials: List<TestimonialsItem>? = null,

    @field:SerializedName("ads")
    val ads: String = "",

    @field:SerializedName("type")
    val type: String = ""
)

data class TestimonialsItem(

    @field:SerializedName("profile_photo")
    val profilePhoto: String = "",

    @field:SerializedName("review_description")
    val reviewDescription: String = "",

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("id")
    val id: String = "",

    @field:SerializedName("age")
    val age: String = "",

    @field:SerializedName("review_title")
    val reviewTitle: String = ""
)

data class TestimonialsMeta(

    @field:SerializedName("api")
    val api: String = "",

    @field:SerializedName("message")
    val message: String = "",

    @field:SerializedName("url")
    val url: String = ""
)
