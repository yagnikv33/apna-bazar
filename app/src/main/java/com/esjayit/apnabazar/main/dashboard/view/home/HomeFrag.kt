package com.esjayit.apnabazar.main.dashboard.view.home

//import com.esjayit.apnabazar.AppConstants.App.BundleData.ADD_DEMAND_CODE
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.ADD_DEMAND_CODE
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.CheckUserActiveResponse
import com.esjayit.apnabazar.data.model.response.HomeScreenListResponse
import com.esjayit.apnabazar.data.model.response.ListItem
import com.esjayit.apnabazar.data.model.response.UserProfileDetailResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.AddDemandAct
import com.esjayit.apnabazar.main.dashboard.view.home.adapter.HomeListingAdapter
import com.esjayit.apnabazar.main.dashboard.view.home.model.HomeVM
import com.esjayit.apnabazar.main.entrymodule.view.SignInAct
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFrag : BaseFrag<FragmentHomeBinding, HomeVM>(Layouts.fragment_home) {

    override val vm: HomeVM by viewModel()
    override val hasProgress: Boolean = false
    private var listingAdapter: HomeListingAdapter? = null
    var homeListAdapter: BaseRvBindingAdapter<ListItem?>? = null
    var homeRvUtil: RvUtil? = null

    override fun init() {
        "USER DATA ${prefs.user}".logE()
        "INSATLL ID ${prefs.installId!!}".logE()
        if (prefs.installId != null) {
            vm.checkUserActiveStatus(userId = prefs.user.userId, installedId = prefs.installId!!)
            vm.getHomeScreen(userId = prefs.user.userId, installedId = prefs.installId.orEmpty())
        }
        if (prefs.user.userId.isNullOrEmpty()) {
            errorToast("Facing issue with user id")
        } else {
            if (prefs.userProfileDetail != null) {
                "already user profile fetched".logE()
                binding.userName.setText(prefs.userProfileDetail.userData?.detail?.uname.toString())
            } else {
                vm.getUserProfile(
                    userId = prefs.user.userId,
                    installedId = prefs.installId.orEmpty()
                )
            }
        }

        editDemandRcv()
    }

    private fun manageUserStatus(obj: CheckUserActiveResponse) {
        //Not Null
        val data = obj.data
        var msg = ""
        if (!data.isActive.isNullOrEmpty() && !data.isMultiDeviceAllow.isNullOrEmpty()) {
            if (data.isActive == "1" && data.isMultiDeviceAllow == "1") {
                //Temp Show Msg
//                successToast(obj.message)
            } else if (data.isActive == "1" && data.isMultiDeviceAllow == "1") {
                msg = obj.message
                showAlert(msg = msg)
            } else if (data.isActive == "0" && data.isMultiDeviceAllow == "0" || data.isActive == "0" && data.isMultiDeviceAllow == "1") {
                msg = obj.message
            }
        }
    }

    //For Show Alert
    private fun showAlert(msg: String) {
        AlertDialog.Builder(requireActivity())
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Okay",
                DialogInterface.OnClickListener { dialog, id ->
                    prefs.clearPrefs()
                    this.startActivity(Intent(requireActivity(), SignInAct::class.java))
                })
            .show()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnAddDemand -> {
                startActivityForResult(AddDemandAct::class.java, requestCode = ADD_DEMAND_CODE)
            }
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    //For Check User Active
                    is CheckUserActiveResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            manageUserStatus(apiRenderState.result)
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }
                    //For Home Screen Message Listing
                    is HomeScreenListResponse -> {
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
//                            successToast("LISTING ${apiRenderState.result.data?.list}")
                            "HOME DATA LISTING ${apiRenderState.result.data?.list}".logE()

                            apiRenderState.result.data?.list?.map {
                                vm.homeListData.add(it)
                            }
                            homeRvUtil?.rvAdapter?.notifyDataSetChanged()
                            // setAdapter(apiRenderState.result.data?.list)
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }
                    //For User Profile Details
                    is UserProfileDetailResponse -> {
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "User Profile Fetched".logE()
                            prefs.userProfileDetail = apiRenderState.result
                            binding.userName.setText(prefs.userProfileDetail.userData?.detail?.uname.toString())
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Home Frag ${apiRenderState.result.message}".logE()
                        }
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
            }
        }
    }

    private fun editDemandRcv() {
        homeListAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_home_item_2,
            list = vm.homeListData,
            br = BR.data,
        )

        homeRvUtil = homeListAdapter?.let {
            RvUtil(
                adapter = it,
                rv = binding.rvHomeListing
            )
        }
    }

    private fun setAdapter(list: List<ListItem?>?) {
        binding.rvHomeListing.layoutManager = LinearLayoutManager(requireContext())
        listingAdapter = HomeListingAdapter(list as List<ListItem>)
        binding.rvHomeListing.adapter = listingAdapter
    }

}