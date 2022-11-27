package com.esjayit.apnabazar.main.dashboard.view.demand

import android.annotation.SuppressLint
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_NO
import com.esjayit.apnabazar.AppConstants.App.BundleData.VIEW_DEMAND_ID
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.ViewDemandItemslistItem
import com.esjayit.apnabazar.data.model.response.ViewDemandRes
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
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

        "ViewDemand: ${prefs.user.userId}, ${prefs.installId}, $did, $dno".logE()

        //For when only view demand
        vm.getViewDemandList(
            userid = prefs.user.userId,
            installid = prefs.installId.orEmpty(),
            demandid = did
        )

        getCurrentDateTime()

        setRv()
    }

    private fun setRv() {
        viewDemandAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_view_demand,
            list = vm.viewDemandList,
            br = BR.data,
            clickListener = { v, t, p ->
                when (v.id) {
                    R.id.ll_sub_header, R.id.tv_subject_sub_header, R.id.tv_rate, R.id.tv_standard_sub_header -> {
                        vm.subjectData.forEach {
                            it?.isTextVisible = false
                        }

                        t?.isTextVisible = !t?.isTextVisible!!

                        rvUtil?.notifyAdapter()
                    }
                    R.id.main_view -> {
                        vm.subjectData.forEach {
                            it?.isTextVisible = false
                        }

                        t?.isTextVisible = !t?.isTextVisible!!

                        rvUtil?.notifyAdapter()
                    }
                }
            },
            viewHolder = { v, t, p ->
                /** Ime DONE Action */
                v.findViewById<EditText>(R.id.edt_qty)
                    .setOnEditorActionListener { v, actionId, event ->

                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            v.hideSoftKeyboard()
                            return@setOnEditorActionListener true
                        }

                        false
                    }
            }
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
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm a")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> {
                finishAct()
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is ViewDemandRes -> {
                        "Response: ${apiRenderState.result}".logE()
//                        val demand = apiRenderState.result.data?.demand
//                        var list: List<ViewDemandItemslistItem?>? = null
//                        val demand = ViewDemandDemand(discountamt = "120", partyname = "Abhishek Bakhai", demanddate = "26-11-2022", itemslist = list?.map {  ViewDemandItemslistItem(thock = "12", itemid = "123", std = "1", amount = "12321312", bunchqty = "11", rate = "1230", subname = "ENGMOOO", qty = "123", rank = "1", subcode = "1", medium = "ENG") }, viewdemanddate = "12-11-2022", demandid = "658", totalamt = "123450", billdate = "12-11-2022", grandtotal = "123440", demandno = "12", viewbilldate = "12-11-2022", roundoff = "0.50", billno = "1")
//                        binding.etDate.setText(demand?.billdate.orEmpty())
//                        binding.txtVTotalAmount.setText("Total Amount  : " + demand?.totalamt.orEmpty())
//                        binding.txtVDiscount.setText("Disc.(12.5 %)   : " + demand?.discountamt.orEmpty())
//                        binding.txtVRoundOff.setText("Round Off   : " + demand?.roundoff.orEmpty())
//                        binding.txtVGrandTotal.setText("Grand Total  : " +demand?.grandtotal.orEmpty())
                        //"Response: ${apiRenderState.result}".logE()

                        vm.viewDemandList.clear()

                        apiRenderState.result.data?.demand?.itemslist?.map {
                            vm.viewDemandList.add(it)
                        }
                        rvUtil?.rvAdapter?.notifyDataSetChanged()

                        "List Size: ${vm.viewDemandList.size}".logE()
                    }
                }
            }
            else -> {

            }
        }
    }
}