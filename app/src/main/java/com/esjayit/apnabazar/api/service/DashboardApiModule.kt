package com.esjayit.apnabazar.api.service

import com.esjayit.BuildConfig
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.ADD_DEMAND
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.DEMAND_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.EDIT_USER_PROFILE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_MEDIUM
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_RETURN_LISTING
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_STANDARD
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_SUBJECT_LIST
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.GET_USER_PROFILE
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.PARTY_LEDGER
import com.esjayit.apnabazar.AppConstants.Api.EndUrl.VIEW_DEMAND_LIST
import com.esjayit.apnabazar.data.model.response.*
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
    ): ViewDemandDetailsResponse

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

    @FormUrlEncoded
    @POST(ADD_DEMAND)
    suspend fun addDemand(
        @Field("demanddate") demanddate: String,
        @Field("userid") userid: String,
        @Field("totalamt") totalamt: String,
        @Field("itemslist") itemslist: Array<SendDemandItem>,
        @Field("installid") installid: String,
        @Field("packagename") appPackgeName: String = BuildConfig.APPLICATION_ID,
        @Field("versioncode") appVerCode: String = BuildConfig.VERSION_CODE.toString(),
    ): CommonResponse
}