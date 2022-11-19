package com.esjayit.apnabazar.helper.util

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class DataUtil {

}
infix fun <B> String?.convertToModel(target: Class<B>): B {
    return Gson().fromJson(this, target)
}
fun Any.convertToString(): String {
    return Gson().toJson(this)
}

fun Long.getDateString(datePattern: String): String {
    val formatter = SimpleDateFormat(datePattern, Locale.getDefault())

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}
