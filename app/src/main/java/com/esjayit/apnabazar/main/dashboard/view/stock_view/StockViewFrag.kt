package com.esjayit.apnabazar.main.dashboard.view.stock_view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.BILL_NO
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_DATE
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_ID
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_LIST_CODE
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.GetReturnLisitngResponse
import com.esjayit.apnabazar.data.model.response.RetunlistItem
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.FragmentStockViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class StockViewFrag : BaseFrag<FragmentStockViewBinding, StockViewVM>(Layouts.fragment_stock_view) {
    override val hasProgress: Boolean = false
    override val vm: StockViewVM by viewModel()
    val progressDialog: CustomProgress by lazy { CustomProgress(requireActivity()) }
    lateinit var returnListAdapter: BaseRvBindingAdapter<RetunlistItem?>
    var rvUtil: RvUtil? = null

    override fun init() {
        progressDialog.showProgress()
        vm.getReturnListData(userId = prefs.user.userId, installedId = prefs.installId!!)

        binding.tvNoData.visibility = View.GONE
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
            binding.btnAddReturn -> {
                startActivityForResult(ReturnListAct::class.java, requestCode = RETURN_LIST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RETURN_LIST_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                vm.getReturnListData(userId = prefs.user.userId, installedId = prefs.installId!!)
            }
        }
    }

    private fun setRcv() {
        BaseRvBindingAdapter(
            layoutId = R.layout.raw_return_list,
            list = vm.returnList,
            br = BR.returnListData,
            clickListener = { v, t, p ->
                //For when only view return
//                vm.getReturnSingleItem(userid = prefs.user.userId, installid = prefs.installId.orEmpty(), itemid = "")

                startActivity(
                    ViewReturnAct::class.java,
                    bundleOf(RETURN_ID to t?.id, RETURN_DATE to t?.billdate, BILL_NO to t?.billno)
                )

            },
            viewHolder = { v, t, p ->
                when (t?.approvecode) {
                    "0" -> {
                        v.findViewById<CardView>(R.id.return_card_demand_status)
                            .setCardBackgroundColor(resources.getColor(R.color.status_yellow))
                    }
                    "1" -> {
                        v.findViewById<CardView>(R.id.return_card_demand_status)
                            .setCardBackgroundColor(resources.getColor(R.color.status_green))
                    }
                }
            }
        ).also { returnListAdapter = it }

        rvUtil = RvUtil(
            adapter = returnListAdapter,
            rv = binding.rvReturnList,
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is GetReturnLisitngResponse -> {
                        vm.returnList.clear()

                        if (apiRenderState.result.statuscode == AppConstants.Status_Code.Success) {

                            if (apiRenderState.result.data?.retunlist.isNullOrEmpty()) {

                                binding.tvNoData.visibility = View.VISIBLE
                            } else {

                                binding.tvNoData.visibility = View.GONE
                                //"Get 5% Return Data ${apiRenderState.result.data}".logE()
                                apiRenderState.result.data?.retunlist?.map {
                                    vm.returnList.add(it)
                                }
                                setRcv()
                            }
                        } else {
                            errorToast(apiRenderState.result.message.toString())
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