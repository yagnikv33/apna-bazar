package com.yudiz.e_cigarette.api

import android.util.Log
import com.google.gson.JsonElement
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

object ErrorUtil : KoinComponent {
    private val retrofit by inject<Retrofit>()

    fun parseError(response: Response<*>): ResponseError {
        val converter = retrofit.responseBodyConverter<ResponseError>(
            ResponseError::class.java,
            arrayOfNulls(0)
        )

        val error: ResponseError

        error = try {
            converter.convert(response.errorBody()!!)!!
        } catch (e: IOException) {
            ResponseError()
        }

        return error
    }
}

class ResponseError {
    var message: String? = null
    private val error: JsonElement? = null

    val errMsg: String?
        get() {
            var msg = ""
            try {
                if (error != null && error.isJsonObject) {
                    val obj = error.asJsonObject
                    val entrySet = obj.entrySet()
                    for ((key, value) in entrySet) {
                        if ((value as JsonElement).isJsonArray) {
                            val arr = value.asJsonArray
                            msg += arr.get(0).asString
                            break
                        }
                        Log.i("error", "getErrMsg: $key $msg")
                    }
                }
                return msg
            } catch (e: Exception) {
                return null
            }

        }

}