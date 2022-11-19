package com.esjayit.apnabazar.main.dashboard.repo

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.api.service.DashboardApiModule
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo

class DashboardRepo(private val apiCall: DashboardApiModule) : BaseRepo() {
    //For Home Screen API Data
    var checkUserActiveData = MutableLiveData(CheckUserActiveResponse())

    suspend fun getMediumList(
        userid: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): MediumResponse? {
        return with(apiCall(executable = {
            apiCall.getMediumList(
                userid = userid,
                installid = installid
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun checkUserActive(
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): CheckUserActiveResponse? {
        return with(apiCall(executable = {
            apiCall.checkUserActive(
                userId = userId,
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Get Home List Messgaes
    suspend fun getHomeList(
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): HomeScreenListResponse? {
        return with(apiCall(executable = {
            apiCall.getHomeMessageList(
                userId = userId,
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }


    //For Get User Profile Details
    suspend fun getUserProfile(
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): UserProfileDetailResponse? {
        return with(apiCall(executable = {
            apiCall.getUserProfile(
                userId = userId,
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //For edit User Profile Details
    suspend fun editProfile(
        userId: String,
        name: String,
        address: String,
        city: String,
        state: String,
        country: String,
        phone1: String,
        phone2: String,
        gstNo: String,
        panNo: String,
        email: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit): EditProfileDetailResponse? {
        return with(apiCall(executable = {
            apiCall.editUserProfile(
                userId = userId,
                uName = name,
                uAddress = address,
                uCity = city,
                uState = state,
                uCountry = country,
                uPhone1 = phone1,
                uPhone2 = phone2,
                uGstNo = gstNo,
                uPanNo = panNo,
                uEmail = email,
                installId = installId)
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}