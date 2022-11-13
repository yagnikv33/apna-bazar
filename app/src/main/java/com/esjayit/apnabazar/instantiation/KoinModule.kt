package com.esjayit.apnabazar.instantiation

import androidx.databinding.library.BuildConfig
import com.google.gson.GsonBuilder
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.api.HeaderHttpInterceptor
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.helper.util.NetworkUtil
import com.esjayit.apnabazar.helper.util.PrefUtil
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.apnabazar.main.entrymodule.repo.EntryRepo
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

    }

    val repoModule = module {
        factory { EntryRepo(get()) }
    }

    val vmModule = module {
        viewModel { EntryVM(get()) }
    }
}