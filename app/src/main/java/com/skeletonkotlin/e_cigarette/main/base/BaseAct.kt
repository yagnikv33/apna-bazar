package com.skeletonkotlin.e_cigarette.main.base

import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.skeletonkotlin.BR
import com.skeletonkotlin.R
import com.skeletonkotlin.e_cigarette.AppConstants.Api.ResponseCode.UNAUTHORIZED_CODE
import com.skeletonkotlin.e_cigarette.AppConstants.Communication.BundleData.IS_UNAUTHORISED
import com.skeletonkotlin.e_cigarette.helper.util.*
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.CANCELLED
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.entrymodule.view.MainAct
import com.smokelaboratory.freedom.Freedom
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class BaseAct<binding : ViewDataBinding, VM : BaseVM>(
    @LayoutRes private val layoutId: Int, private val fragFactory: FragmentFactory? = null
) : AppCompatActivity(), View.OnClickListener {

    protected val prefs by inject<PrefUtil>()
    protected lateinit var binding: binding
    protected var freedom: Freedom? = null
    private var progress: ObservableField<Boolean>? = null

    protected abstract val vm: VM?
    protected abstract fun renderState(apiRenderState: ApiRenderState)
    protected abstract val hasProgress: Boolean
    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        fragFactory?.let {
            supportFragmentManager.fragmentFactory = it
        }
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<binding>(this, layoutId).apply {
            lifecycleOwner = this@BaseAct

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
                            logout(true)
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
            setVariable(BR.click, this@BaseAct)

            if (hasProgress) {
                progress = ObservableField()
                setVariable(BR.progress, progress)
            }
        }

        init()
    }

    fun logout(isUnauthorised: Boolean = false) {
        prefs.clearPrefs()

        startActivity(
            MainAct::class.java,
            bundleOf(IS_UNAUTHORISED to isUnauthorised),
            listOf(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }

    fun startActivity(
        act: Class<*>,
        bundle: Bundle? = null,
        flags: List<Int>? = null,
        shouldAnimate: Boolean = true
    ) {
        val intent = Intent(this, act)

        if (bundle != null)
            intent.putExtras(bundle)

        if (!flags.isNullOrEmpty())
            flags.forEach {
                intent.addFlags(it)
            }

        if (shouldAnimate)
            startActivity(
                intent, ActivityOptions.makeCustomAnimation(
                    applicationContext,
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
        val intent = Intent(this, act)

        if (bundle != null)
            intent.putExtras(bundle)

        if (!flags.isNullOrEmpty())
            flags.forEach {
                intent.addFlags(it)
            }

        if (shouldAnimate)
            startActivityForResult(
                intent, requestCode,
                ActivityOptions.makeCustomAnimation(
                    applicationContext,
                    R.anim.fade_in,
                    R.anim.fade_out
                ).toBundle()
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

    fun showDialogFrag(dialFrag: DialogFragment, bundle: Bundle?) {
        dialFrag.arguments = bundle
        dialFrag.show(supportFragmentManager, "")
    }

    fun showProgress() {
        progress?.set(true)
    }

    fun hideProgress() {
        progress?.set(false)
    }

    inline fun <reified T : Fragment> addFrag(
        container: Int,
        addToBackStack: Boolean = false,
        shouldAnimate: Boolean = true,
        bundle: Bundle? = null
    ) {
        supportFragmentManager.commit {
            if (shouldAnimate)
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            if (addToBackStack)
                addToBackStack(T::class.java.name)
            add<T>(container, args = bundle)
        }
    }

    fun addFrag(
        fragment: Fragment,
        container: Int,
        addToBackStack: Boolean = false,
        shouldAnimate: Boolean = true,
        bundle: Bundle? = null
    ) {
        supportFragmentManager.commit {
            if (shouldAnimate)
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            if (addToBackStack)
                addToBackStack(fragment::class.java.name)
            if (bundle != null)
                fragment.arguments = bundle

            add(container, fragment)
        }
    }

    inline fun <reified T : Fragment> replaceFrag(
        container: Int,
        addToBackStack: Boolean = false,
        shouldAnimate: Boolean = true,
        bundle: Bundle? = null
    ) {
        supportFragmentManager.commit {
            if (shouldAnimate)
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            if (addToBackStack)
                addToBackStack(T::class.java.name)
            replace<T>(container, args = bundle)
        }
    }

    fun replaceFrag(
        fragment: Fragment,
        container: Int,
        addToBackStack: Boolean = false,
        shouldAnimate: Boolean = true,
        bundle: Bundle? = null
    ) {
        supportFragmentManager.commit {
            if (shouldAnimate)
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            if (addToBackStack)
                addToBackStack(fragment::class.java.name)
            if (bundle != null)
                fragment.arguments = bundle

            replace(container, fragment)
        }
    }

    fun popFrag() {
        supportFragmentManager.popBackStack()
    }

    fun finishAct() {
        currentFocus?.hideSoftKeyboard()
        finish()
    }

    fun delayedExecutor(millis: Long, executable: () -> Unit) {
        lifecycleScope.launch {
            delay(millis)
            executable.invoke()
        }
    }

    fun onContainerBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            popFrag()
        else
            finishAct()
    }

    private fun isApplicationInBackground(): Boolean {
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
        val runningTasks =
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningTasks(1)
        if (runningTasks.isNotEmpty())
            return runningTasks[0].topActivity?.packageName != packageName
        return false
//        } else {
//            val runningTasks = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).appTasks
//            if (runningTasks.isNotEmpty())
//                return runningTasks[0].taskInfo.topActivity.packageName != packageName
//            return false
//        }
    }

    override fun onClick(v: View) {

    }


}
