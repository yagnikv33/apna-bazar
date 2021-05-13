package com.skeletonkotlin.e_cigarette.main.entrymodule.view

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.*
import com.google.firebase.iid.FirebaseInstanceId
import com.skeletonkotlin.e_cigarette.Colors
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.databinding.MainActBinding
import com.skeletonkotlin.e_cigarette.helper.util.LocationFetchUtil
import com.skeletonkotlin.e_cigarette.helper.util.MiscUtil.bgExecutor
import com.skeletonkotlin.e_cigarette.helper.util.logE
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.entrymodule.model.MainActVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

class MainAct : BaseAct<MainActBinding, MainActVM>(Layouts.act_main/*, FragFactory()*/) {

    private lateinit var mediaPicker: EasyImage
    override val vm: MainActVM by viewModel()
    override val hasProgress: Boolean = true

    override fun init() {

        delayedExecutor(5000) {
            successToast("delayed toast")
        }

        binding.url =
            "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"

        mediaPicker = EasyImage.Builder(this).allowMultiple(true).build()

        prefs.authToken = "s"

        //location fetch
        locationFetchUtil = LocationFetchUtil(this@MainAct,
            shouldRequestPermissions = true,
            shouldRequestOptimization = true,
            callbacks = object : LocationFetchUtil.Callbacks {
                override fun onSuccess(location: Location) {
                }

                override fun onFailed(locationFailedEnum: LocationFetchUtil.LocationFailedEnum) {
                }
            })

        binding.tv.text = buildSpannedString {
            bold { append("hi") }
            underline { append(" how are you ") }
            strikeThrough { append(" 2 ") }
            color(ContextCompat.getColor(this@MainAct, Colors.colorAccent)) { append(" nice ") }
            inSpans(object : ClickableSpan() {
                override fun onClick(textView: View) {
                    errorToast("asdasd")
                }

                override fun updateDrawState(ds: TextPaint) {
//                    ds.isUnderlineText = false
                }
            }) { bold { append(" !!!!!") } }
        }

        binding.tv.highlightColor = Color.TRANSPARENT
        /**
         * added for click to work
         */
        binding.tv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun getFcmToken() {
        bgExecutor(this, executable = {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { result ->
                if (result.token.isEmpty())
                //handle error
                else {
                    prefs.fcmToken = result.token
                    //send in api
                }
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.tv.id ->
                vm.login("a", "a")
//                mediaPicker.openGallery(this)
            binding.btFrag.id -> {
//                addFrag<DemoFrag>(R.id.frame)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaPicker.handleActivityResult(requestCode, resultCode, data, this, object :
            EasyImage.Callbacks {
            override fun onCanceled(source: MediaSource) {

            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {

            }

            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                imageFiles.size.toString().logE()
            }
        })
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.Loading -> showProgress()
            is ApiRenderState.ApiSuccess<*> -> {
                hideProgress()
                successToast("Success")
            }
            is ApiRenderState.ValidationError -> {
                apiRenderState.message?.let {
                    errorToast(getString(it)) { isDismissed ->
                        Toast.makeText(this, "dismissed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}