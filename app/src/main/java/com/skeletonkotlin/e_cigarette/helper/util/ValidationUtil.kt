package com.skeletonkotlin.e_cigarette.helper.util

import android.util.Patterns
import androidx.annotation.StringDef
import java.util.regex.Pattern

object ValidationUtil {

    const val maxPasswordLength = 6
    val EMAIL_ADDRESS_PATTERN: String = Patterns.EMAIL_ADDRESS.pattern()
    val MOBILE_PATTERN: String = Patterns.PHONE.pattern()

    @StringDef("MM/dd/yyyy", "EEE dd/MM/yy")
    annotation class DatePatterns

}

fun String.isPasswordValid() = length >= ValidationUtil.maxPasswordLength

fun String.isEmailValid() =
    Pattern.compile(ValidationUtil.EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE).matcher(this)
        .matches()

fun String.isMobileValid() =
    Pattern.compile(ValidationUtil.MOBILE_PATTERN, Pattern.CASE_INSENSITIVE).matcher(this).matches()

