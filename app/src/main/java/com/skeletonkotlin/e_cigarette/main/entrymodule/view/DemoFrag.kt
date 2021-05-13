package com.skeletonkotlin.e_cigarette.main.entrymodule.view

import android.Manifest
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.skeletonkotlin.BR
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.databinding.DemoFragBinding
import com.skeletonkotlin.e_cigarette.helper.util.logE
import com.skeletonkotlin.e_cigarette.main.base.BaseFrag
import com.skeletonkotlin.e_cigarette.main.base.BaseVM
import com.skeletonkotlin.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.smokelaboratory.freedom.freedom

class DemoFrag(val text: String) : BaseFrag<DemoFragBinding, BaseVM>(Layouts.frag_demo) {

        private lateinit var backPressCallback: OnBackPressedCallback
        private val backPressDispatcher by lazy { requireActivity().onBackPressedDispatcher }

    override val vm: BaseVM? = null

    override fun init() {
        backPressCallback = backPressDispatcher.addCallback(this) {
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

            backPressCallback.isEnabled = false
            /**
             * to call parent's back press => [backPressDispatcher].onBackPressed()
             * callback in child should be disabled before that, else it enters into infinite loop
             */
        }

        binding.vp.adapter =
            BaseRvBindingAdapter(
                Layouts.row_demo,
                mutableListOf("1", "2", "3", "4", "5", "6"),
                br = BR.content
            )

        //permission request
        freedom = freedom(requireActivity(), this) {
            permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            userResponse = { areAllGranted, _ ->
                "TAG FRAG $areAllGranted".logE()
            }

            shouldShowSettingsPopup = true
            popupConfigurations {
                title = "Location fetching failed"
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }

    override val hasProgress: Boolean
        get() = TODO("Not yet implemented")
}