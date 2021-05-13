package com.skeletonkotlin.e_cigarette.helper.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build

import java.util.Locale

/**
 * to enable it, add the following in base act
 *      override fun attachBaseContext(base: Context) {
 *           super.attachBaseContext(LocaleUtil.onAttach(base, prefs.selectedLanguage))
 *      }
 *
 * https://gunhansancar.com/change-language-programmatically-in-android/
 */
object LocaleUtil {

    fun onAttach(context: Context, defaultLanguage: String): Context {
        return setLocale(context, defaultLanguage)
    }

    fun setLocale(context: Context, language: String?): Context {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            updateResources(context, language)
        else updateResourcesLegacy(context, language)

    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }
}