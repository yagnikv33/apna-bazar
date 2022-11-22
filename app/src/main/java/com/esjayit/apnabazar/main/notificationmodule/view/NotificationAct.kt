package com.esjayit.apnabazar.main.notificationmodule.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.cardview.widget.CardView
import androidx.transition.Visibility
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.DashboardAct
import com.esjayit.apnabazar.main.entrymodule.model.EntryVM
import com.esjayit.apnabazar.main.entrymodule.view.GetYourCodeAct
import com.esjayit.apnabazar.main.notificationmodule.model.NotificationVM
import com.esjayit.databinding.ActivityNotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationAct : BaseAct<ActivityNotificationBinding, NotificationVM>(Layouts.activity_notification) {

    override val vm: NotificationVM by viewModel()
    override val hasProgress: Boolean = true
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }
    lateinit var notificationListAdapter: BaseRvBindingAdapter<NotificationlistItem?>
    var rvUtil: RvUtil? = null

    override fun init() {
        vm?.getNotificationList(userId = prefs.user.userId!!, installId = prefs.installId!!)
    }

    private fun setRcv() {
        BaseRvBindingAdapter(
            layoutId = R.layout.raw_noti_list,
            list = vm.notificationListData,
            br = BR.notiListData,
            clickListener = { v, t, p ->
                //Read API Call
                if (t?.isread == "0") {
                    vm?.readNotification(inboxId = t?.inboxid.toString(), userId = prefs.user.userId!!, installId = prefs.installId!!)
                } else  {
                    errorToast("Already Read Message")
                }
            },
            viewHolder = { v, t, p ->
                if(!t?.url.isNullOrEmpty()) {
                    v.findViewById<CardView>(R.id.download_btn).visibility = VISIBLE
                } else {
                    v.findViewById<CardView>(R.id.download_btn).visibility = INVISIBLE
                }
            }
        ).also { notificationListAdapter = it }

        rvUtil = RvUtil(
            adapter = notificationListAdapter,
            rv = binding.rvNotificationList,
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is NotificationListResponse -> {
                        val statusCode = apiRenderState.result.statuscode
                        progressDialog.hideProgress()
                        if (statusCode == AppConstants.Status_Code.Success) {
                            successToast(apiRenderState.result.message.toString())
                            "Response: ${apiRenderState.result.data}".logE()
                            //Dummmy Data (Remove after check)
                             vm.notificationListData.add(NotificationlistItem(datetime = "01-40-2000", isread = "1", title = "Hello User", body = "Dear Customer, Due to Diwali festival Apnabazar is Closed from 23/10/2022 to 27/10/2022", inboxid = "231313", url = "sdadad" ))
                            vm.notificationListData.add(NotificationlistItem(datetime = "01-40-2000", isread = "0", title = "Hello User", body = "Dear Customer, Due to Diwali festival Apnabazar is Closed from 23/10/2022 to 27/10/2022", inboxid = "231313", url = "" ))
                            apiRenderState.result.data?.inboxlist?.map {
                                vm.notificationListData.add(it)
                            }
                            setRcv()
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Pwd ACT ${apiRenderState.result.message}".logE()
                        }
                    }

                    //For Read Notification Response
                    is ReadNotificationResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            successToast(apiRenderState.result.message)
                            "Response: ${apiRenderState.result.data}".logE()
                        } else {
                            errorToast(apiRenderState.result.message)
                            "Error : Pwd ACT ${apiRenderState.result.message}".logE()
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
}