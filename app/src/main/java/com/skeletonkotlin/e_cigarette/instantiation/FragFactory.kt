package com.skeletonkotlin.e_cigarette.instantiation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.skeletonkotlin.e_cigarette.main.entrymodule.view.DemoFrag

/**
 * If your Fragment has a default empty constructor, there’s no need to use a FragmentFactory.
 * If however, your Fragment takes in arguments in its constructor, a FragmentFactory must be used, otherwise a Fragment.InstantiationException will be thrown,
 * since the default FragmentFactory that will be used will not know how to instantiate an instance of your Fragment in cases like config-change.
 */

class FragFactory : FragmentFactory() {

    /**
     * to get fragment instances, store variables here and get using a custom method of factory class
     */

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DemoFrag::class.java.name -> DemoFrag("constructor")
            else -> super.instantiate(classLoader, className)
        }
    }
}