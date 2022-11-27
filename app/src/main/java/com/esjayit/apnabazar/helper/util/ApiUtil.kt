package com.esjayit.apnabazar.helper.util

import com.esjayit.apnabazar.data.model.response.DummyAddDemand
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun String.getRequestBodyString() = toRequestBody("application_view/string".toMediaTypeOrNull())
