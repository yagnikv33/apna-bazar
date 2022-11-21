package com.esjayit.apnabazar.helper.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.Prefs.AUTH_TOKEN
import com.esjayit.apnabazar.data.model.response.LoginResponse
import com.esjayit.apnabazar.data.model.response.UserProfileDetailResponse


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

    var user: LoginResponse
        get() = prefs.getString("USER_INFO", "") convertToModel LoginResponse::class.java
        set(data) {
            prefEditor.putString("USER_INFO", data.convertToString())
            prefEditor.apply()
        }

    var userProfileDetail: UserProfileDetailResponse
        get() = prefs.getString("USER_DETAIL_INFO", "") convertToModel UserProfileDetailResponse::class.java
        set(data) {
            prefEditor.putString("USER_DETAIL_INFO", data.convertToString())
            prefEditor.apply()
        }

    var firstTime: Boolean
        get() = prefs.getBoolean("FIRST_TIME", true)
        set(value) {
            prefEditor.putBoolean("FIRST_TIME", value)
            prefEditor.apply()
        }

    var installId: String?
        get() = prefs.getString("INSTALL_ID", "")
        set(value) {
            prefEditor.putString("INSTALL_ID", value)
            prefEditor.apply()
        }

    var playerId: String?
        get() = prefs.getString("PLAYER_ID", "")
        set(value) {
            prefEditor.putString("PLAYER_ID", value)
            prefEditor.apply()
        }

    fun hasKey(key: String) = prefs.contains(key)



    fun clearPrefs() {
        prefEditor.clear()
        prefEditor.apply()
    }
}
