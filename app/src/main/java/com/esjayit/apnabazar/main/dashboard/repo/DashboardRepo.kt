package com.esjayit.apnabazar.main.dashboard.repo

import androidx.lifecycle.MutableLiveData
import com.esjayit.BuildConfig
import com.esjayit.apnabazar.api.service.DashboardApiModule
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.main.base.ApiResult
import com.esjayit.apnabazar.main.base.BaseRepo
import com.google.gson.JsonArray
import com.google.gson.JsonObject

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
    ): ViewDemandRes? {
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
        onError: (ApiResult<Any>) -> Unit
    ): CommonResponse? {
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
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getPartyLedger(
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): PartyLedgerResponse? {
        return with(apiCall(executable = {
            apiCall.getPartyLedger(
                userId = userId,
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //For 5% Listing
    suspend fun getReturnListing(
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): GetReturnLisitngResponse? {
        return with(apiCall(executable = {
            apiCall.getReturnListing(
                userId = userId,
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getDemandList(
        userId: String,
        installId: String,
        onError: (ApiResult<Any>) -> Unit
    ): DemandListResponse? {
        return with(apiCall(executable = {
            apiCall.demandList(
                userId = userId,
                installId = installId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Add Demand
//    suspend fun addDemand(
//        itemList: JsonObject,
//        demanddate: String,
//        userid: String,
//        totalamt: String,
//        installid: String,
//        onError: (ApiResult<Any>) -> Unit
//    ): CommonResponse? {
//        return with(apiCall(executable = {
//            apiCall.addDemand(
//                userid = userid,
//                demanddate = demanddate,
//                totalamt = totalamt,
//                installid = installid,
//                itemList = itemList
//            )
//        })) {
//            if (data == null)
//                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
//            this.data
//        }
//    }

    //Add Demand
    suspend fun addDemandString(
        demandData : AddDemand,
        onError: (ApiResult<Any>) -> Unit
    ): CommonResponse? {
        return with(apiCall(executable = {
            apiCall.addDemandString(
              demandData
            )

        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Fetch Data For Edit Demand
    suspend fun getEditDemandData(
        userid: String,
        demandid: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): EditDemandDataResponse? {
        return with(apiCall(executable = {
            apiCall.getDemandEditData(
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

    //For Edit Demand API
    suspend fun editDemand(
        itemList: Array<DummyEditDemand>,
        demandid: String,
        demanddate: String,
        userid: String,
        totalamt: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): CommonResponse? {
        return with(apiCall(executable = {
            apiCall.editDemand(
                userid = userid,
                demandid = demandid,
                demanddate = demanddate,
                totalamt = totalamt,
                installid = installid,
                itemList = JsonObject().apply {
                    itemList.forEach {
                        this.addProperty("itemid", it.itemid)
                        this.addProperty("qty", it.qty)
                        this.addProperty("rate", it.rate)
                        this.addProperty("amount", it.amount)
                        this.addProperty("bunchqty", it.bunchqty)
                    }
                }
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }


    //For 5% Item Listing
    suspend fun getReturnItemListing(
        userId: String,
        installId: String,
        userMedium: String,
        standard: String,
        onError: (ApiResult<Any>) -> Unit
    ): GetReturnItemListResponse? {
        return with(apiCall(executable = {
            apiCall.getReturnItemList(
                userId = userId,
                installId = installId,
                standard = standard,
                userMedium = userMedium
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //For 5% Return Single Details
    suspend fun getReturnSingleDetail(
        userId: String,
        installId: String,
        itemId: String,
        onError: (ApiResult<Any>) -> Unit
    ): SingleItemResponse? {
        return with(apiCall(executable = {
            apiCall.getReturnSingleItem(
                userId = userId,
                installId = installId,
                itemId = itemId
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Add 5% Return Book
    suspend fun addReturnBook(
        returnList: Array<DummyReturn>,
        billDate: String,
        userid: String,
        billAmount: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): CommonResponse? {
        return with(apiCall(executable = {
            apiCall.addReturnBook(
                userid = userid,
                billAmount = billAmount,
                billDate = billDate,
                installid = installid,
                returnList = JsonObject().apply {
                    returnList.forEach {
                        this.addProperty("itemid", it.itemid)
                        this.addProperty("buyqty", it.buyqty)
                        this.addProperty("maxretu", it.maxretu)
                        this.addProperty("rate", it.rate)
                        this.addProperty(
                            "retuqty",
                            it.maxretu
                        ) //Please change with edit text Field return qty
                    }
                }
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    //Get 5% Return View
    suspend fun getReturnViewData(
        userid: String,
        returnid: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): ViewBookReturnDataResponse? {
        return with(apiCall(executable = {
            apiCall.viewReturn(
                userid = userid,
                installid = installid,
                returnid = returnid
            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }

    suspend fun getSingleEditItemDetail(
        userid: String,
        itemid: String,
        installid: String,
        onError: (ApiResult<Any>) -> Unit
    ): SingleEditItemResponse? {
        return with(apiCall(executable = {
            apiCall.getSingleEditItemDetail(
                userid = userid,
                itemid = itemid,
                installid = installid

            )
        })) {
            if (data == null)
                onError.invoke(ApiResult(null, resultType, error, resCode = resCode))
            this.data
        }
    }
}