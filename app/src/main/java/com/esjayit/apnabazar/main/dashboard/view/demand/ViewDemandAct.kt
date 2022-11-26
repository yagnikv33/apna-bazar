package com.esjayit.apnabazar.main.dashboard.view.demand

import android.view.View
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_NO
import com.esjayit.apnabazar.AppConstants.App.BundleData.VIEW_DEMAND_ID
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.ViewDemandRes
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseAct
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

    override fun init() {
        "GetListData: ${AppConstants.App.itemlistItem}".logE()

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


    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is ViewDemandRes -> {
                        "Response: ${apiRenderState.result}".logE()
                    }
                }
            }
            else -> {

            }
        }
    }
}