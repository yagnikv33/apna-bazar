package com.esjayit.apnabazar.instantiation

import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.api.HeaderHttpInterceptor
import com.esjayit.apnabazar.api.service.DashboardApiModule
import com.esjayit.apnabazar.api.service.EntryApiModule
import com.esjayit.apnabazar.api.service.NotificationApiModule
import com.esjayit.apnabazar.helper.util.PrefUtil
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.repo.DashboardRepo
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.apnabazar.main.dashboard.view.home.model.HomeVM
import com.esjayit.apnabazar.main.dashboard.view.profile.model.ProfileVM
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.apnabazar.main.dashboard.view.user_ledger.model.UserLedgerVM
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.apnabazar.main.entrymodule.repo.EntryRepo
import com.esjayit.apnabazar.main.notificationmodule.model.NotificationVM
import com.esjayit.apnabazar.main.notificationmodule.repo.NotificationRepo
import com.google.gson.GsonBuilder
import com.esjayit.apnabazar.helper.util.NetworkUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KoinModule {

    val utilModule = module {
        single { PrefUtil(get()) }
        single { NetworkUtil() }
    }

    val apiModule = module {
        factory {
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                )
                .addInterceptor(HeaderHttpInterceptor(get()))
                .build()
        }

        single {
            Retrofit.Builder().baseUrl(AppConstants.Api.TEST_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setPrettyPrinting().create()
                    )
                )
                .client(get()).build()
        }

        single { get<Retrofit>().create(EntryApiModule::class.java) }
        single { get<Retrofit>().create(DashboardApiModule::class.java) }
        single { get<Retrofit>().create(NotificationApiModule::class.java) }

    }

    val repoModule = module {
        factory { EntryRepo(get()) }
        factory { DashboardRepo(get()) }
        factory { NotificationRepo(get()) }
        factory { HttpLoggingInterceptor(get()) }
    }

    val vmModule = module {
        viewModel { EntryVM(get()) }
        viewModel { DashboardVM(get()) }
        viewModel { HomeVM(get()) }
        viewModel { StockViewVM(get()) }
        viewModel { DemandListVM(get()) }
        viewModel { UserLedgerVM(get()) }
        viewModel { ProfileVM(get()) }
        viewModel { NotificationVM(get()) }
    }
}