package com.esjayit.apnabazar.main.dashboard.view.demand

import android.annotation.SuppressLint
import android.view.View
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_NO
import com.esjayit.apnabazar.AppConstants.App.BundleData.VIEW_DEMAND_ID
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.ViewDemandItemslistItem
import com.esjayit.apnabazar.data.model.response.ViewDemandRes
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.ActivityViewDemandBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ViewDemandAct :
    BaseAct<ActivityViewDemandBinding, DemandListVM>(Layouts.activity_view_demand) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false

    var did: String = ""
    var dno: String = ""
    var viewDemandAdapter: BaseRvBindingAdapter<ViewDemandItemslistItem?>? = null
    var rvUtil: RvUtil? = null

    override fun init() {

        val bundle = intent.extras

        did = bundle?.getString(VIEW_DEMAND_ID).toString()
        dno = bundle?.getString(DEMAND_NO).toString()

        binding.etDemandNo.setText(dno)

        //For when only view demand
        vm.getViewDemandList(
            userid = prefs.user.userId,
            installid = prefs.installId.orEmpty(),
            demandid = did
        )

        setRv()

        binding.tvNoData.visibility = View.GONE
    }

    private fun setRv() {
        viewDemandAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_view_demand,
            list = vm.viewDemandList,
            br = BR.data
        )

        rvUtil = viewDemandAdapter?.let {
            RvUtil(
                adapter = it,
                rv = binding.rvViewDemand,
                decoration = RvItemDecoration.buildDecoration(
                    this,
                    R.dimen._1sdp,
                    color = R.color.grey
                )
            )
        }
    }

    private fun getCurrentDateTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
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


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is ViewDemandRes -> {
                        // "Response: viewDemand - ${apiRenderState.result.data?.demand?.itemslist}".logE()

                        vm.viewDemandList.clear()

                        if (apiRenderState.result.data?.demand?.demanddate == null) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE

                            apiRenderState.result.data.demand.itemslist?.map {
                                vm.viewDemandList.add(it)
                            }
                            rvUtil?.rvAdapter?.notifyDataSetChanged()

                            apiRenderState.result.data.let {
                                binding.etDate.setText(it?.demand?.demanddate.orEmpty())
                                binding.txtVTotalAmount.text =
                                    "Total Amount  : " + it?.demand?.totalamt.orEmpty()
                                binding.txtVDiscount.text =
                                    "Disc.(12.5 %)   : " + it?.demand?.discountamt.orEmpty()
                                binding.txtVRoundOff.text =
                                    "Round Off   :  " + it?.demand?.roundoff.orEmpty()
                                binding.txtVGrandTotal.text =
                                    "Grand Total  : " + it?.demand?.grandtotal.orEmpty()
                            }
                        }
                    }
                }
            }
            else -> {

            }
        }
    }
}