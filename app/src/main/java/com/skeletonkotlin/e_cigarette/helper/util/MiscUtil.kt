package com.skeletonkotlin.e_cigarette.helper.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.util.Base64
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.gson.Gson
import com.skeletonkotlin.e_cigarette.Styles
import com.skeletonkotlin.e_cigarette.helper.util.MiscUtil.throttleFirst
import kotlinx.coroutines.*
import java.lang.reflect.Type
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.util.*

object MiscUtil {

    private const val versionAcronym = "v : "

    fun rateApp(act: Activity) {
        val url = "https://play.google.com/store/apps/details?id=" + act.packageName
        try {
            act.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    url.toUri()
                )
            )
        } catch (e: Exception) {
            url.openWebPage(act)
        }
    }

    fun shareApp(act: Activity) {
        try {
            act.startActivity(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=" + act.packageName
                )
            })
        } catch (e: Exception) {
        }
    }

    fun composeEmail(
        act: Activity,
        addresses: Array<String>,
        subject: String,
        text: String? = null
    ) {
        act.startActivity(Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        })
    }

    fun getVersionCode(con: Context): String {
        try {
            val packageInfo = con.packageManager.getPackageInfo(
                con.packageName,
                0
            )

            return versionAcronym + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                packageInfo.longVersionCode
            else
                packageInfo.versionCode.toLong()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getVersionName(con: Context): String {
        try {
            return versionAcronym + con.packageManager.getPackageInfo(
                con.packageName,
                0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    fun hideStatusBar(act: Activity) {
        act.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun getDeviceWidth(act: Activity): Int {
        val displayMetrics = DisplayMetrics()
        act.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getDeviceHeight(act: Activity): Int {
        val displayMetrics = DisplayMetrics()
        act.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getHashKey(context: Context): String? {
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.DEFAULT)
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

        return null
    }

    fun getInstalledLanguages(): LocaleListCompat {
        return ConfigurationCompat.getLocales(Resources.getSystem().configuration)
    }

    fun arePlayServicesAvailable(act: Activity, reqCode: Int): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(act)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(act, status, reqCode).show()
            }
            return false
        }
        return true
    }

    fun bgExecutor(
        lifecycleOwner: LifecycleOwner,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        executable: suspend () -> Unit
    ): Job {
        return lifecycleOwner.lifecycleScope.launch(dispatcher) {
            executable.invoke()
        }
    }

    fun <T> throttleLatest(
        intervalMs: Long = 300L,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        executable: (T) -> Unit
    ): (T) -> Unit {
        var job: Job? = null
        var latestParam: T
        return { param: T ->
            latestParam = param
            if (job?.isCompleted != false)
                job = scope.launch(dispatcher) {
                    delay(intervalMs)
                    executable(latestParam)
                }
        }
    }

    /**
     * Constructs a function that processes input data and passes the first data to [executable] and skips all new data for the next [skipMs].
     */
    fun <T> throttleFirst(
        skipMs: Long = 300L,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        executable: (T) -> Unit
    ): (T) -> Unit {
        var job: Job? = null
        return { param: T ->
            if (job?.isCompleted != false)
                job = scope.launch(dispatcher) {
                    executable(param)
                    delay(skipMs)
                }
        }
    }

    /**
     * Constructs a function that processes input data and passes it to [executable] only if there's no new data for at least [waitMs]
     */
    fun <T> debounce(
        waitMs: Long = 300L,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        executable: (T) -> Unit
    ): (T) -> Unit {
        var job: Job? = null
        return { param: T ->
            job?.cancel()
            job = scope.launch(dispatcher) {
                delay(waitMs)
                executable(param)
            }
        }
    }

    inline fun <reified T : ViewDataBinding> createDialog(
        context: Context,
        @LayoutRes layoutId: Int,
        isCancelable: Boolean = false
    ): Pair<Dialog, T>? {
        try {
            Dialog(context, Styles.DialogStyle).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                DataBindingUtil.inflate<T>(
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                    layoutId,
                    null,
                    false
                ).let {
                    setContentView(it.root)

                    setCancelable(isCancelable)
                    if (isCancelable) {
                        it.root.setOnClickListener {
                            dismiss()
                        }
                    }

                    return Pair(this, it)
                }
            }
        } catch (e: Exception) {
            return null
        }
    }

    fun getIdFromName(context: Context, name: String): Int {
        return context.resources.getIdentifier(
            name,
            "id",
            context.packageName
        )
    }
}

fun View.unFocusParent(onFocus: Boolean = false) {
    setOnTouchListener { v, _ ->

        v.parent.requestDisallowInterceptTouchEvent(
            if (onFocus)
                v.hasFocus()
            else
                true
        )

        false
    }
}

fun Double.format(digitsAfterDecimal: Int): String {
    return DecimalFormat("0.${"#".repeat(digitsAfterDecimal)}").format(this)    //#. instead of 0. if we need output as 1.00
}

fun View.applyTint(color: Int, isAndroidRes: Boolean = false) {
    val colorFilter = if (isAndroidRes) ContextCompat.getColor(context, color) else color
    val mode = android.graphics.PorterDuff.Mode.SRC_IN

    if (this is ImageView)
        setColorFilter(
            colorFilter,
            mode
        )
    else
        background.colorFilter = PorterDuffColorFilter(colorFilter, mode)
}

fun View.clearTint() {
    if (this is ImageView)
        colorFilter = null
    else
        background.colorFilter = null
}

fun String.firstCapital(): String {
    return this.substring(0, 1).toUpperCase(Locale.ROOT) + this.substring(1, this.length)
}

fun TextView.textColor(context: Context, textColor: Int) {
    setTextColor(ContextCompat.getColor(context, textColor))
}

fun View.hideSoftKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun View.showSoftKeyboard() {
    if (requestFocus())
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            this,
            InputMethodManager.SHOW_IMPLICIT
        )
}

fun String.dialNumber(context: Activity) {
    try {
        context.startActivity(Intent(Intent.ACTION_DIAL).also {
            it.data = "tel:$this".toUri()
        })
    } catch (e: Exception) {
    }
}

fun String.openWebPage(context: Activity) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, toUri()))
    } catch (e: Exception) {
    }
}

fun String.isPackageExists(context: Context): Boolean {
    val pm = context.packageManager
    val packages = pm.getInstalledApplications(0)
    for (packageInfo in packages)
        if (packageInfo.packageName == this) return true
    return false
}

infix fun <B> String?.convertToModel(target: Class<B>): B {
    return Gson().fromJson(this, target)
}

infix fun <B> String?.convertToModel(target: Type): B {
    return Gson().fromJson(this, target)
}

fun Any.convertToString(): String {
    return Gson().toJson(this)
}

/*inline fun <reified B> convertJsonToModel(jArr: JsonArray): B {
    return Gson().fromJson(jArr, object : TypeToken<B>() {}.type)
}*/

fun View.onClickListener(lifecycleOwner: LifecycleOwner, listener: (View) -> Unit) {
    val throttle = throttleFirst<Unit>(
        skipMs = 1000L,
        scope = lifecycleOwner.lifecycleScope
    ) {
        listener.invoke(this)
    }

    this.setOnClickListener {
        throttle(Unit)
    }
}



