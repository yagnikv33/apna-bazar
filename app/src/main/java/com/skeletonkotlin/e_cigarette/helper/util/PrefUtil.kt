package com.skeletonkotlin.e_cigarette.helper.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.skeletonkotlin.e_cigarette.AppConstants.Prefs.AUTH_TOKEN
import com.skeletonkotlin.e_cigarette.AppConstants.Prefs.FCM_TOKEN
import com.skeletonkotlin.e_cigarette.AppConstants.Prefs.ROOM_KEY
import com.skeletonkotlin.e_cigarette.AppConstants.Prefs.USER_INFO
import com.skeletonkotlin.e_cigarette.Strings
import com.skeletonkotlin.e_cigarette.data.model.response.LoginResModel

class PrefUtil(context: Context) {

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        context.getString(Strings.app_name),
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
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

    var roomKey: String
        get() = prefs.getString(ROOM_KEY, "").orEmpty()
        set(key) {
            prefEditor.putString(ROOM_KEY, key)
            prefEditor.apply()
        }

    var fcmToken: String?
        get() = prefs.getString(FCM_TOKEN, "")
        set(fcmToken) {
            prefEditor.putString(FCM_TOKEN, fcmToken)
            prefEditor.apply()
        }

    var userInfo: LoginResModel
        get() = prefs.getString(USER_INFO, "") convertToModel LoginResModel::class.java
        set(data) {
            prefEditor.putString(USER_INFO, data.convertToString())
            prefEditor.apply()
        }

    fun hasKey(key: String) = prefs.contains(key)

    fun clearPrefs() {
        prefs.all.forEach {
            prefEditor.remove(it.key)
        }
        prefEditor.apply()
    }
}
