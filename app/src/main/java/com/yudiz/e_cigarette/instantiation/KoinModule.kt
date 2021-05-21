package com.yudiz.e_cigarette.instantiation

import androidx.databinding.library.BuildConfig
import com.google.gson.GsonBuilder
import com.yudiz.e_cigarette.AppConstants
import com.yudiz.e_cigarette.api.HeaderHttpInterceptor
import com.yudiz.e_cigarette.api.service.EntryApiModule
import com.yudiz.e_cigarette.api.service.HomeApiModule
import com.yudiz.e_cigarette.helper.util.NetworkUtil

import com.yudiz.e_cigarette.main.entrymodule.model.EntryVM
import com.yudiz.e_cigarette.main.entrymodule.model.MainActVM
import com.yudiz.e_cigarette.main.entrymodule.repo.EntryRepo
import com.yudiz.e_cigarette.main.entrymodule.repo.MainActRepo
import com.yudiz.e_cigarette.main.home.model.HomeVM
import com.yudiz.e_cigarette.main.home.repo.HomeRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KoinModule {

    val utilModule = module {
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
                .addInterceptor(HeaderHttpInterceptor())
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