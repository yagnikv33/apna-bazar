package com.esjayit.apnabazar.main.base

import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.helper.util.*
import com.esjayit.apnabazar.main.common.ApiRenderState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.inject

abstract class BaseAct<binding : ViewDataBinding, VM : BaseVM>(
    @LayoutRes private val layoutId: Int, private val fragFactory: FragmentFactory? = null
) : AppCompatActivity(), View.OnClickListener {

    protected val prefs by inject<PrefUtil>()
   protected lateinit var binding: binding
    val nwUtil by inject<NetworkUtil>()
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

                lifecycleScope.launch {
                    it.state().collect {
                        renderState(it)
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

        lifecycleScope.launch {
            nwUtil.observe(this@BaseAct) { hasInternet ->
                if (!hasInternet) {
                    errorToast("No internet connection")
                }
            }
        }
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
        val runningTasks =
            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningTasks(1)
        if (runningTasks.isNotEmpty())
            return runningTasks[0].topActivity?.packageName != packageName
        return false
    }

    override fun onClick(v: View) {

    }
}
