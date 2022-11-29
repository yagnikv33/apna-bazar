package com.esjayit.apnabazar.main.dashboard.view.stock_view

import android.annotation.SuppressLint
import android.view.View
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_DATE
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_ID
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.RetunlistItem
import com.esjayit.apnabazar.data.model.response.RetutranlistItem
import com.esjayit.apnabazar.data.model.response.ViewBookReturnDataResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.ActivityViewReturnBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ViewReturnAct :
    BaseAct<ActivityViewReturnBinding, StockViewVM>(Layouts.activity_view_return) {

    override val vm: StockViewVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    var viewReturnAdapter: BaseRvBindingAdapter<RetutranlistItem>? = null
    var rvUtil: RvUtil? = null
    var returnId = ""
    var returnDate = ""
    var returnModel: RetunlistItem? = null

    override fun init() {

        val bundle = intent?.extras

        returnId = bundle?.getString(RETURN_ID).toString()
        returnDate = bundle?.getString(RETURN_DATE).toString()

        progressDialog.showProgress()

        binding.etDemandNo.setText(returnId)
        binding.etDate.setText(returnDate)

        //Api Call
        vm.viewReturnBook(
            userid = prefs.user.userId,
            installid = prefs.installId.orEmpty(),
            returnid = returnId
        )

        setRv()

        binding.tvNoData.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    private fun setRv() {
        viewReturnAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_view_return,
            list = vm.viewReturnList,
            br = BR.data
        )

        rvUtil = RvUtil(
            adapter = viewReturnAdapter!!,
            rv = binding.rvViewReturn,
            decoration = RvItemDecoration.buildDecoration(this, R.dimen._1sdp, color = R.color.grey)
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> {
                finishAct()
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
                    is ViewBookReturnDataResponse -> {
                        vm.viewReturnList.clear()
//                        "Response: viewBookReturn : ${apiRenderState.result}".logE()

                        if (apiRenderState.result.data?.jsonMemberReturn?.retutranlist?.isNullOrEmpty() == true) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE

                            apiRenderState.result.data?.jsonMemberReturn?.retutranlist?.map {
                                if (it != null) {
                                    vm.viewReturnList.add(it)
                                }
                            }

                            rvUtil?.rvAdapter?.notifyDataSetChanged()
                        }
                        progressDialog.hideProgress()
                    }
                }
            }
            else -> {
                progressDialog.hideProgress()
            }
        }
    }
}