package com.esjayit.apnabazar.api

import com.esjayit.apnabazar.AppConstants.Api.Value.COMPANY_ID
import com.esjayit.apnabazar.helper.util.PrefUtil
import okhttp3.Interceptor
import okhttp3.Response

class HeaderHttpInterceptor(private var prefUtil: PrefUtil)  : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().run {
            newBuilder()
                .header("Accept", "application/json").apply {
                    header("company-id", COMPANY_ID)
                    prefUtil.authToken?.let {
                        header("Authorization", "Bearer $it")
                    }
                }.method(method, body).build()
        })
    }
}