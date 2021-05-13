package com.skeletonkotlin.e_cigarette

import androidx.multidex.MultiDexApplication
import com.skeletonkotlin.e_cigarette.instantiation.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initTasks()
    }

    private fun initTasks() {

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    KoinModule.utilModule,
                    KoinModule.vmModule,
                    KoinModule.apiModule,
                    KoinModule.repoModule
                )
            )
        }
        //        FirebaseApp.initializeApp(this)
    }
}
