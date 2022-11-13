package com.esjayit.apnabazar.helper.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.Prefs.AUTH_TOKEN


class PrefUtil(context: Context) {

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context.getString(R.string.app_name),
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val prefEditor: SharedPreferences.Editor

    init {
        prefEditor = prefs.edit()
    }

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN, "")
        set(authToken) {
            prefEditor.putString(AUTH_TOKEN, authToken)
            prefEditor.apply()
        }

//    var userInfo: LoginResModel
//        get() = prefs.getString(USER_INFO, "") convertToModel LoginResModel::class.java
//        set(data) {
//            prefEditor.putString(USER_INFO, data.convertToString())
//            prefEditor.apply()
//        }

    fun hasKey(key: String) = prefs.contains(key)

    fun clearPrefs() {
        prefEditor.clear()
        prefEditor.apply()
    }
}
