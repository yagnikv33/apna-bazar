package com.esjayit.apnabazar.main.notificationmodule.view

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.view.View.*
import android.webkit.DownloadListener
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.NotificationListResponse
import com.esjayit.apnabazar.data.model.response.NotificationlistItem
import com.esjayit.apnabazar.data.model.response.ReadNotificationResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.notificationmodule.model.NotificationVM
import com.esjayit.databinding.ActivityNotificationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.app.AlertDialog
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.webkit.URLUtil
import com.esjayit.apnabazar.main.dashboard.view.demand.AddDemandAct
import com.esjayit.apnabazar.main.dashboard.view.demand.ViewDemandAct
import android.app.DownloadManager as DownloadManager1


class NotificationAct : BaseAct<ActivityNotificationBinding, NotificationVM>(Layouts.activity_notification) {

    override val vm: NotificationVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }
    lateinit var notificationListAdapter: BaseRvBindingAdapter<NotificationlistItem?>
    var rvUtil: RvUtil? = null

    lateinit var context: Context
    lateinit var activity: NotificationAct
    lateinit var downloadListener: DownloadListener
    var writeAcess = false
    var downloadPage=""

    override fun init() {
        context = applicationContext
        activity =this
        checkWriteAccess()
        vm?.getNotificationList(userId = prefs.user.userId!!, installId = prefs.installId!!)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnBack -> {
                finishAct()
            }
        }
    }

    private fun checkWriteAccess() {
        if (Build.VERSION.SDK_INT < 29) {
            //below android 11
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        } else {
//            progressDialog?.showProgress()
            writeAcess = true
        }
    }

    //For Checking Permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (!(grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(
                        this,
                        "Permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                    writeAcess = false
                } else {
                    Toast.makeText(this,"Waiting for download",Toast.LENGTH_LONG).show()
                    writeAcess = true
                }
            }
        }
    }

    //Set Recycle View
    private fun setRcv() {
        BaseRvBindingAdapter(
            layoutId = R.layout.raw_noti_list,
            list = vm.notificationListData,
            br = BR.notiListData,
            clickListener = { v, t, p ->
                //Read API Call
                //Temp Check PDF Download
//                downloadPDF(url = "http://img.esjaysoftware.com/commonimages/Apnabazar/Apnabazar_18_(27-07-2022).pdf")
                when (v.id) {
                    R.id.download_btn -> {
                        if (!t?.url.isNullOrEmpty()) {
                            downloadPDF(url = t?.url!!)
                        }
                    }
                    R.id.ll_main_noti_list -> {
                        if (t?.isread == "0") {
                            vm?.readNotification(inboxId = t?.inboxid.toString(), userId = prefs.user.userId!!, installId = prefs.installId!!)
                        } else {
                            errorToast("Already Read Message")
                        }
                    }
                    else -> {
                        if (t?.isread == "0") {
                            vm?.readNotification(inboxId = t?.inboxid.toString(), userId = prefs.user.userId!!, installId = prefs.installId!!)
                        } else {
                            errorToast("Already Read Message")
                        }
                    }
                }
            },
            viewHolder = { v, t, p ->
                if(!t?.url.isNullOrEmpty()) {
                    v.findViewById<ConstraintLayout>(R.id.noti_download_view).visibility = VISIBLE
                } else {
                    v.findViewById<ConstraintLayout>(R.id.noti_download_view).visibility = GONE
                }
            }
        ).also { notificationListAdapter = it }

        rvUtil = RvUtil(
            adapter = notificationListAdapter,
            rv = binding.rvNotificationList,
        )
    }

    //For Download PDF File
    fun downloadPDF(url: String) {
        downloadPage = url
        Toast.makeText(this,"Downloading...",Toast.LENGTH_LONG).show()
        createDownloadListener()

        onDownloadComplete()
    }

    //Create Download File
    private fun createDownloadListener() {
        val request = android.app.DownloadManager.Request(Uri.parse(downloadPage))
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val dManager = getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
        if (writeAcess)
            dManager.enqueue(request)
        else {
            Toast.makeText(this, "Unable to download", Toast.LENGTH_LONG).show()
            checkWriteAccess()
        }
    }

    //On Complete Download
    private fun onDownloadComplete() {
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(context,"Pdf downloaded",Toast.LENGTH_LONG).show()
            }
        }
        registerReceiver(onComplete, IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    //Manage API Calls
    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is NotificationListResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statuscode
                        progressDialog.hideProgress()
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "Notification Response: ${apiRenderState.result.data}".logE()
                            //Dummmy Data (Remove after check)
//                             vm.notificationListData.add(NotificationlistItem(datetime = "01-40-2000", isread = "1", title = "Hello User", body = "Dear Customer, Due to Diwali festival Apnabazar is Closed from 23/10/2022 to 27/10/2022", inboxid = "231313", url = "sdadad" ))
//                            vm.notificationListData.add(NotificationlistItem(datetime = "01-40-2000", isread = "0", title = "Hello User", body = "Dear Customer, Due to Diwali festival Apnabazar is Closed from 23/10/2022 to 27/10/2022", inboxid = "231313", url = "" ))
                            apiRenderState.result.data?.inboxlist?.map {
                                vm.notificationListData.add(it)
                            }
                            setRcv()
                            successToast(apiRenderState.result.message.toString())
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                            "Error : Pwd ACT ${apiRenderState.result.message}".logE()
                        }
                    }

                    //For Read Notification Response
                    is ReadNotificationResponse -> {
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
//                            successToast(apiRenderState.result.message)
                            "Response: ${apiRenderState.result.data}".logE()
                        } else {
//                            errorToast(apiRenderState.result.message)
                            "Error : Noti ACT ${apiRenderState.result.message}".logE()
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