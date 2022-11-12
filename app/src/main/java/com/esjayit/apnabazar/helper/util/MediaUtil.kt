package com.esjayit.apnabazar.helper.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.decode.DataSource
import coil.request.CachePolicy
import coil.request.LoadRequestBuilder
import coil.request.Request
import coil.transform.CircleCropTransformation
import com.esjayit.apnabazar.Drawables
import java.io.File

object MediaUtil {
}

@BindingAdapter(
    "path",
    "progress",
    "placeholder",
    "isCircular",
    "disableCache",
    requireAll = false
)
fun loadImage(
    iv: ImageView,
    imagePath: String?,
    pb: ProgressBar? = null,
    placeholder: Drawable?,
    isCircular: Boolean,
    disableCache: Boolean
) {

    if (imagePath == null)
        return

    val coilReqBuilder: LoadRequestBuilder.() -> Unit = {
        if (disableCache) {
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.DISABLED)
        } else {
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
        }

        val fallback = placeholder ?: ContextCompat.getDrawable(iv.context, Drawables.ic_placeholder)
        error(fallback)
        placeholder(fallback)
        listener(object : Request.Listener {
            override fun onCancel(data: Any) {
                pb?.visibility = View.GONE
            }

            override fun onError(data: Any, throwable: Throwable) {
                pb?.visibility = View.GONE
            }

            override fun onStart(data: Any) {
                pb?.visibility = View.VISIBLE
            }

            override fun onSuccess(data: Any, source: DataSource) {
                pb?.visibility = View.GONE
            }
        })

        if (isCircular)
            transformations(CircleCropTransformation())

//        crossfade(true)
    }

    if (!imagePath.startsWith("http") && !imagePath.startsWith("content://"))
        iv.load(File(imagePath), builder = coilReqBuilder)
    else
        iv.load(imagePath, builder = coilReqBuilder)
}
