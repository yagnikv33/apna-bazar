package com.esjayit.apnabazar.main.dashboard.view.demand

import androidx.cardview.widget.CardView
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.DemandListItem
import com.esjayit.apnabazar.data.model.response.DemandListResponse
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.FragmentDemandListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemandListFrag :
    BaseFrag<FragmentDemandListBinding, DemandListVM>(Layouts.fragment_demand_list) {

    override val hasProgress: Boolean = false
    override val vm: DemandListVM by viewModel()
    lateinit var demandListAdapter: BaseRvBindingAdapter<DemandListItem?>
    var rvUtil: RvUtil? = null

    override fun init() {
        vm.getDemandList(userid = prefs.user.userId, installId = prefs.installId.orEmpty())
        setRcv()
    }

    private fun setRcv() {
        BaseRvBindingAdapter(
            layoutId = R.layout.raw_demand_list,
            list = vm.demandList,
            br = BR.demandListData,
            clickListener = { v, t, p ->
                //Edit demand code navigation HERE
            },
            viewHolder = { v, t, p ->
                when (t?.demandcolorcode) {
                    "0" -> {
                        v.findViewById<CardView>(R.id.card_demand_status)
                            .setCardBackgroundColor(resources.getColor(R.color.status_yellow))
                    }
                    "1" -> {
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
                        "Response: ${apiRenderState.result.data}".logE()

                        apiRenderState.result.data?.demandlist?.map {
                            vm.demandList.add(it)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}