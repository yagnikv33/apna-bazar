package com.esjayit.apnabazar.helper.util

import com.google.gson.Gson

class DataUtil {

}
infix fun <B> String?.convertToModel(target: Class<B>): B {
    return Gson().fromJson(this, target)
}
fun Any.convertToString(): String {
    return Gson().toJson(this)
}