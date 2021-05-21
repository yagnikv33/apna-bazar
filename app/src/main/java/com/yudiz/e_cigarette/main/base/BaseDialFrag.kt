package com.yudiz.e_cigarette.main.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.yudiz.BR
import com.yudiz.R
import com.yudiz.e_cigarette.AppConstants.Api.ResponseCode.UNAUTHORIZED_CODE
import com.yudiz.e_cigarette.Styles
import com.yudiz.e_cigarette.helper.util.ToastUtil
import com.yudiz.e_cigarette.main.base.BaseRepo.ApiResultType.CANCELLED
import com.yudiz.e_cigarette.main.common.ApiRenderState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseDialFrag<binding : ViewDataBinding, VM : BaseVM> : DialogFragment(),
    View.OnClickListener {

    protected lateinit var binding: binding

    protected abstract val layoutId: Int
    protected abstract val vm: VM?
    protected abstract fun init()
    protected abstract fun renderState(apiRenderState: ApiRenderState)
    protected abstract val hasProgress: Boolean
    private var progress: ObservableField<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, Styles.AppTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<binding>(inflater, layoutId, container, false).apply {
            lifecycleOwner = this@BaseDialFrag
            vm?.let {
                setVariable(BR.vm, it)

                lifecycleScope.launch {
                    it.state().collect {
                        renderState(it)
                    }
                }

                lifecycleScope.launch {
                    it.apiError.collect {
                        if (it.resCode == UNAUTHORIZED_CODE)
                            (requireActivity() as BaseAct<ViewDataBinding, BaseVM>).logout(true)

                        else
                            if (it.resultType != CANCELLED) {
                                hideProgress()
                                it.error?.let {
                                    errorToast(it)
                                }
                            }
                    }
                }
            }
            setVariable(BR.click, this@BaseDialFrag)
        }

        if (hasProgress) {
            progress = ObservableField()
            binding.setVariable(BR.progress, progress)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    fun startActivity(
        act: Class<*>,
        bundle: Bundle? = null,
        flags: List<Int>? = null,
        shouldAnimate: Boolean = true
    ) {
        val intent = Intent(activity, act)

        if (bundle != null)
            intent.putExtras(bundle)

        if (!flags.isNullOrEmpty())
            flags.forEach {
                intent.addFlags(it)
            }

        if (shouldAnimate)
            startActivity(
                intent,
                ActivityOptions.makeCustomAnimation(
                    context,
                    R.anim.fade_in,
                    R.anim.fade_out
                ).toBundle()
            )
        else
            startActivity(intent)
    }

    fun startActivityForResult(
        act: Class<*>,
        requestCode: Int = 0,
        bundle: Bundle? = null,
        flags: List<Int>? = null,
        shouldAnimate: Boolean = true
    ) {
        val intent = Intent(activity, act)

        if (bundle != null)
            intent.putExtras(bundle)

        if (!flags.isNullOrEmpty())
            flags.forEach {
                intent.addFlags(it)
            }

        if (shouldAnimate)
            startActivityForResult(
                intent,
                requestCode,
                ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out)
                    .toBundle()
            )
        else
            startActivityForResult(intent, requestCode)
    }


    fun errorToast(message: String, callback: ((Boolean) -> Unit)? = null) {
        ToastUtil.errorSnackbar(message, binding.root, callback)
    }

    fun successToast(message: String, callback: ((Boolean) -> Unit)? = null) {
        ToastUtil.successSnackbar(message, binding.root, callback)
    }

    fun showProgress() {
        progress?.set(true)
    }

    fun hideProgress() {
        progress?.set(false)
    }

    fun delayedExecutor(millis: Long, executable: () -> Unit) {
        lifecycleScope.launch {
            delay(millis)
            executable.invoke()
        }
    }

    override fun onClick(v: View) {

    }

}
