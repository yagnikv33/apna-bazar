package com.esjayit.apnabazar.main.dashboard.repo

import androidx.lifecycle.MutableLiveData
import com.esjayit.apnabazar.api.service.DashboardApiModule
import com.esjayit.apnabazar.data.model.response.GetDemandDataResponse
import com.esjayit.apnabazar.data.model.response.MediumResponse
import com.esjayit.apnabazar.data.model.response.StandardResponse
import com.esjayit.apnabazar.data.model.response.ViewDemandDetailsResponse
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

    suspend fun getStandard(
        userid: String,
        userMedium: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): StandardResponse? {
        return with(apiCall(executable = {
            apiCall.getStandardList(
                userid = userid,
                installid = installid,
                userMedium = userMedium
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getSubjectList(
        userid: String,
        userMedium: String,
        standard: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): GetDemandDataResponse? {
        return with(apiCall(executable = {
            apiCall.getSubjectList(
                userid = userid,
                installid = installid,
                standard = standard,
                userMedium = userMedium
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getViewDemandList(
        userid: String,
        demandid: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): ViewDemandDetailsResponse? {
        return with(apiCall(executable = {
            apiCall.getViewDemandList(
                userid = userid,
                installid = installid,
                demandid = demandid
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