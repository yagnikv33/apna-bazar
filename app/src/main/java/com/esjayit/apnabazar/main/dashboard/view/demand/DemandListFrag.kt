package com.esjayit.apnabazar.main.dashboard.view.demand

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.ADD_DEMAND_CODE
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_DATE
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_NO
import com.esjayit.apnabazar.AppConstants.App.BundleData.EDIT_DEMAND_DATA
import com.esjayit.apnabazar.AppConstants.App.BundleData.FOR_EDIT_DEMAND
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_TO_SEARCH
import com.esjayit.apnabazar.AppConstants.App.BundleData.VIEW_DEMAND_ID
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.DemandListItem
import com.esjayit.apnabazar.data.model.response.DemandListResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.FragmentDemandListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


//class DemandListFrag :
//    BaseFrag<FragmentDemandListBinding, DemandListVM>(Layouts.fragment_demand_list),
//    IOnBackPressed {
class DemandListFrag :
    BaseFrag<FragmentDemandListBinding, DemandListVM>(Layouts.fragment_demand_list) {

    override val hasProgress: Boolean = false
    override val vm: DemandListVM by viewModel()
    lateinit var demandListAdapter: BaseRvBindingAdapter<DemandListItem?>
    var rvUtil: RvUtil? = null
    var usedBackBtn: Boolean = false

    override fun init() {
        vm.getDemandList(userid = prefs.user.userId, installId = prefs.installId.orEmpty())
        setRcv()
        binding.tvNoData.visibility = View.GONE
        //Using BroadCast for api calling (When Add Demand tappd at that time call this)
//        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(myBroadcastReceiver,IntentFilter("thisIsForMyFragment"));
    }

//    private val myBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
////            Toast.makeText(activity, "Broadcast received!", Toast.LENGTH_SHORT).show()
//            vm.getDemandList(userid = prefs.user.userId, installId = prefs.installId.orEmpty())
//            setRcv()
//            binding.tvNoData.visibility = View.GONE
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RETURN_TO_SEARCH) {
            if (resultCode === Activity.RESULT_OK) {
                vm.getDemandList(userid = prefs.user.userId, installId = prefs.installId.orEmpty())
            }
        } else if (requestCode === ADD_DEMAND_CODE) {
            vm.getDemandList(userid = prefs.user.userId, installId = prefs.installId.orEmpty())
//            setRcv()
        }
    }

//    override fun onBackPressed(): Boolean {
//        return if (usedBackBtn) {
//            //action not popBackStack
//            usedBackBtn = false
//            true
//        } else {
//            usedBackBtn = true
//            false
//        }
//    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
            binding.btnAddReturn -> {
                startActivityForResult(AddDemandAct::class.java, requestCode = ADD_DEMAND_CODE)
            }
        }
    }

    private fun setRcv() {
        BaseRvBindingAdapter(
            layoutId = R.layout.raw_demand_list,
            list = vm.demandList,
            br = BR.demandListData,
            clickListener = { v, t, p ->

                when (t?.demandcolorcode) {
                    "0" -> {
                        //For when edit demand
                        when (v.id) {
                            R.id.card_edit_status -> {
                                val intent = Intent(requireActivity(), AddDemandAct::class.java)
                                intent.putExtra(EDIT_DEMAND_DATA, t.did)
                                intent.putExtra(FOR_EDIT_DEMAND, true)
                                intent.putExtra(DEMAND_DATE, t.demanddate)
                                startActivityForResult(intent, RETURN_TO_SEARCH)
//                                startActivity(
//                                    AddDemandAct::class.java,
//                                    bundle = bundleOf(
//                                        EDIT_DEMAND_DATA to t.did,
//                                        FOR_EDIT_DEMAND to true,
//                                        DEMAND_DATE to t.demanddate
//                                    )
//                                )
                            }
                            else -> {
                                val intent = Intent(requireActivity(), ViewDemandAct::class.java)
                                intent.putExtra(VIEW_DEMAND_ID, t.did)
                                intent.putExtra(DEMAND_NO, t.demandno)
                                startActivityForResult(intent, RETURN_TO_SEARCH)
//                                startActivity(
//                                    ViewDemandAct::class.java,
//                                    bundle = bundleOf(
//                                        VIEW_DEMAND_ID to t.did,
//                                        DEMAND_NO to t.demandno
//                                    )
//                                )
                            }
                        }
                    }
                    "1" -> {
                        //For when only view demand
                        startActivity(
                            ViewDemandAct::class.java,
                            bundle = bundleOf(VIEW_DEMAND_ID to t.did, DEMAND_NO to t.demandno)
                        )
                    }
                }
                //For when only view demand
//                vm.getViewDemandList(userid = prefs.user.userId, installid = prefs.installId.orEmpty(), demandid = "")
                //For when edit demand
//                vm.getEditDemandData(userid = prefs.user.userId, installid = prefs.installId.orEmpty(), demandid = "")
                "DemandListData: $t".logE()
            },
            viewHolder = { v, t, p ->
                when (t?.demandcolorcode) {
                    "0" -> {
                        v.findViewById<CardView>(R.id.card_edit_status).visibility = View.VISIBLE

                        v.findViewById<CardView>(R.id.card_demand_status)
                            .setCardBackgroundColor(resources.getColor(R.color.status_yellow))
                    }
                    "1" -> {
                        v.findViewById<CardView>(R.id.card_edit_status).visibility = View.GONE
                        v.findViewById<CardView>(R.id.card_demand_status)
                            .setCardBackgroundColor(resources.getColor(R.color.status_green))
                    }
                }
            }
        ).also { demandListAdapter = it }

        rvUtil = RvUtil(
            adapter = demandListAdapter,
            rv = binding.rvDemandList,
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is DemandListResponse -> {
                        //"Response: ${apiRenderState.result.data}".logE()
                        vm.demandList.clear()
                        if (apiRenderState.result.data?.demandlist.isNullOrEmpty()) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE
                            apiRenderState.result.data?.demandlist?.map {
                                vm.demandList.add(it)
                            }
                        }
                        rvUtil?.rvAdapter?.notifyDataSetChanged()
                    }
                }
            }
            ApiRenderState.Idle -> {

            }
            ApiRenderState.Loading -> {

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