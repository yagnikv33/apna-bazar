package com.yudiz.e_cigarette.main.entrymodule.view

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.yudiz.BR
import com.yudiz.databinding.DemoFragBinding
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.main.base.BaseFrag
import com.yudiz.e_cigarette.main.base.BaseVM
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState

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

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }

    override val hasProgress: Boolean
        get() = TODO("Not yet implemented")
}