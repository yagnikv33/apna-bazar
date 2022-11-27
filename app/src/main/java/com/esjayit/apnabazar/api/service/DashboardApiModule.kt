package com.esjayit.apnabazar.api.service

import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.ADD_BOOK_RETURN
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.ADD_DEMAND
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.DEMAND_EDIT_DATA
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.DEMAND_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.EDIT_DEMAND
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.EDIT_USER_PROFILE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_ITEM_DETAIL
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_MEDIUM
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_RETURN_ITEM_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_RETURN_LISTING
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_RETURN_SINGLE_ITEM_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_STANDARD
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_SUBJECT_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_USER_PROFILE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.PARTY_LEDGER
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.VIEW_DEMAND_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.VIEW_PARTY_RETURN
import com.esjayit.apnabazar.data.model.response.*
import com.google.gson.JsonObject
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.*

interface DashboardApiModule {
    @FormUrlEncoded
    @POST(AppConstants.Api.EndUrl.CHECK_USER_ACTIVE)
    suspend fun checkUserActive(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId: String
    ): CheckUserActiveResponse

    @FormUrlEncoded
    @POST(AppConstants.Api.EndUrl.GET_HOME_DATA)
    suspend fun getHomeMessageList(
        @Field("userid") userId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("installid") installId: String
    ): HomeScreenListResponse

    @FormUrlEncoded
    @POST(GET_MEDIUM)
    suspend fun getMediumList(
        @Field("userid") userid: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): MediumResponse

    @FormUrlEncoded
    @POST(GET_STANDARD)
    suspend fun getStandardList(
        @Field("userid") userid: String,
        @Field("medium") userMedium: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): StandardResponse

    @FormUrlEncoded
    @POST(GET_SUBJECT_LIST)
    suspend fun getSubjectList(
        @Field("userid") userid: String,
        @Field("medium") userMedium: String,
        @Field("standard") standard: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): GetDemandDataResponse

    @FormUrlEncoded
    @POST(VIEW_DEMAND_LIST)
    suspend fun getViewDemandList(
        @Field("userid") userid: String,
        @Field("demandid") demandid: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): ViewDemandRes

    @FormUrlEncoded
    @POST(GET_USER_PROFILE)
    suspend fun getUserProfile(
        @Field("userid") userId: String,
        @Field("installid") installId: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): UserProfileDetailResponse

    @FormUrlEncoded
    @POST(EDIT_USER_PROFILE)
    suspend fun editUserProfile(
        @Field("userid") userId: String,
        @Field("uname") uName: String,
        @Field("uaddress") uAddress: String,
        @Field("ucity") uCity: String,
        @Field("ustate") uState: String,
        @Field("ucountry") uCountry: String,
        @Field("uphone1") uPhone1: String,
        @Field("uphone2") uPhone2: String,
        @Field("gstno") uGstNo: String,
        @Field("panno") uPanNo: String,
        @Field("uemail") uEmail: String,
        @Field("installid") installId: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): CommonResponse

    //For Party Ledger API
    @FormUrlEncoded
    @POST(PARTY_LEDGER)
    suspend fun getPartyLedger(
        @Field("userid") userId: String,
        @Field("installid") installId: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): PartyLedgerResponse

    //For Get 5% Lising
    @FormUrlEncoded
    @POST(GET_RETURN_LISTING)
    suspend fun getReturnListing(
        @Field("userid") userId: String,
        @Field("installid") installId: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): GetReturnLisitngResponse

    @FormUrlEncoded
    @POST(DEMAND_LIST)
    suspend fun demandList(
        @Field("userid") userId: String,
        @Field("installid") installId: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
    ): DemandListResponse

    @POST(ADD_DEMAND)
    suspend fun addDemandData(
        @Body requestJson: JsonObject
    ): CommonResponse

    @POST(ADD_DEMAND)
    suspend fun addDemand(
        @Field("demanddate") demanddate: String,
        @Field("userid") userid: String,
        @Field("totalamt") totalamt: String,
        @Query("itemslist") itemList: JsonObject,
        @Field("installid") installid: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
    ): CommonResponse

    //Data for Demand Edit
    @FormUrlEncoded
    @POST(DEMAND_EDIT_DATA)
    suspend fun getDemandEditData(
        @Field("userid") userid: String,
        @Field("demandid") demandid: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): EditDemandDataResponse

    //For when Edit Demand
    @FormUrlEncoded
    @POST(EDIT_DEMAND)
    suspend fun editDemand(
        @Field("demanddate") demanddate: String,
        @Field("demandid") demandid: String,
        @Field("userid") userid: String,
        @Field("totalamt") totalamt: String,
        @Query("itemslist") itemList: JsonObject,
        @Field("installid") installid: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
    ): CommonResponse


    //For Get 5% Item Lising
    @FormUrlEncoded
    @POST(GET_RETURN_ITEM_LIST)
    suspend fun getReturnItemList(
        @Field("userid") userId: String,
        @Field("installid") installId: String,
        @Field("medium") userMedium: String,
        @Field("standard") standard: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): GetReturnItemListResponse

    //For Get 5% Return Single Item
    @FormUrlEncoded
    @POST(GET_RETURN_SINGLE_ITEM_LIST)
    suspend fun getReturnSingleItem(
        @Field("userid") userId: String,
        @Field("installid") installId: String,
        @Field("itemid") itemId: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): GetReturnSingleDetailResponse

    //Add 5% Return Book
    @FormUrlEncoded
    @POST(ADD_BOOK_RETURN)
    suspend fun addReturnBook(
        @Field("billdate") billDate: String,
        @Field("userid") userid: String,
        @Field("billamount") billAmount: String,
        @Query("retutranlist") returnList: JsonObject,
        @Field("installid") installid: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
    ): CommonResponse

    //View 5% Return
    @FormUrlEncoded
    @POST(VIEW_PARTY_RETURN)
    suspend fun viewReturn(
        @Field("userid") userid: String,
        @Field("returnid") returnid: String,
        @Field("installid") installid: String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): ViewBookReturnDataResponse

    @FormUrlEncoded
    @POST(GET_ITEM_DETAIL)
    suspend fun getSingleEditItemDetail(
        @Field("userid") userid:String,
        @Field("itemid") itemid:String,
        @Field("installid") installid:String,
        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
    ): SingleEditItemResponse

//    @FormUrlEncoded
//    @POST(RETURN_TABLE_DATA)
//    suspend fun getReturnListTableData(
//        @Field("userid") userid: String,
//        @Field("standard") standard: String,
//        @Field("medium") userMedium: String,
//        @Field("installid") installid: String,
//        @Field("versioncode") versioncode: String = BuildConfig.VERSION_CODE.toString(),
//        @Field("packagename") packagename: String = BuildConfig.APPLICATION_ID,
//    ): GetReturnResponse
}