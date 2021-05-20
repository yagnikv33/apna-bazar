package com.skeletonkotlin.e_cigarette.instantiation

import androidx.databinding.library.BuildConfig
import com.google.gson.GsonBuilder
import com.skeletonkotlin.e_cigarette.AppConstants
import com.skeletonkotlin.e_cigarette.api.HeaderHttpInterceptor
import com.skeletonkotlin.e_cigarette.api.service.EntryApiModule
import com.skeletonkotlin.e_cigarette.api.service.HomeApiModule
import com.skeletonkotlin.e_cigarette.helper.util.NetworkUtil
import com.skeletonkotlin.e_cigarette.helper.util.PrefUtil
import com.skeletonkotlin.e_cigarette.main.entrymodule.model.EntryVM
import com.skeletonkotlin.e_cigarette.main.entrymodule.model.MainActVM
import com.skeletonkotlin.e_cigarette.main.entrymodule.repo.EntryRepo
import com.skeletonkotlin.e_cigarette.main.entrymodule.repo.MainActRepo
import com.skeletonkotlin.e_cigarette.main.home.model.HomeVM
import com.skeletonkotlin.e_cigarette.main.home.repo.HomeRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KoinModule {

    val utilModule = module {
        single { PrefUtil(get()) }
        single { NetworkUtil(get()) }
    }

    val apiModule = module {
        factory {
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    )
                )
                .addInterceptor(HeaderHttpInterceptor(get()))
                .build()
        }

        single {
            Retrofit.Builder().baseUrl(AppConstants.Api.BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setPrettyPrinting().create()
                    )
                )
                .client(get()).build()
        }

        single { get<Retrofit>().create(EntryApiModule::class.java) }
        single { get<Retrofit>().create(HomeApiModule::class.java) }

    }

    val repoModule = module {
        factory { MainActRepo(get()) }
        factory { EntryRepo(get()) }
        factory { HomeRepo(get()) }
    }

    val vmModule = module {
        viewModel { MainActVM(get()) }
        viewModel { EntryVM(get()) }
        viewModel { HomeVM(get()) }
    }
}