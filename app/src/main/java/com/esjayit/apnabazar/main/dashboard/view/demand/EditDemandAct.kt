package com.esjayit.apnabazar.main.dashboard.view.demand

import android.app.AlertDialog
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.EDIT_DEMAND_DATA
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.ActivityEditBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditDemandAct : BaseAct<ActivityEditBinding, DemandListVM>(Layouts.activity_edit) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false
    lateinit var editDemandAdapter: BaseRvBindingAdapter<ItemslistItem?>
    var rvUtil: RvUtil? = null
    var standardDialog: AlertDialog.Builder? = null

    private var mediumLanguage = mutableListOf<String>()
    private var castedMediumLanguageList: Array<String>? = null

    private var selectStandard = mutableListOf<String>()
    private var castedSelectStandardList: Array<String>? = null


    var subjectItem: String = ""
    var mediumItem: String = ""
    var searchKeyword: String = ""
    val localSubjectData = mutableListOf<ItemlistItem?>()
    var clickedPosition = -1
    private lateinit var debounceListener: (String) -> Unit
    private val addDemandList = hashSetOf<DummyAddDemand>()
    private lateinit var debounceListenerForRcv: (String) -> Unit

    override fun init() {
        val bundleData = intent?.extras
        vm.apply {
            getMediumList(userid = prefs.user.userId, installid = prefs.installId.orEmpty())
        }

        setSelectDropdown()
        getCurrentDateTime()

        vm.getEditDemandData(
            userid = prefs.user.userId,
            installid = prefs.installId.orEmpty(),
            demandid = bundleData?.getString(EDIT_DEMAND_DATA).toString()
        )
    }

    private fun getCurrentDateTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm a")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

   /* private fun setMediumDropdown() {
        mediumDialog = AlertDialog.Builder(this)
        mediumDialog?.setItems(castedMediumLanguageList) { _, which ->
            binding.etModule.setText(castedMediumLanguageList?.get(which))
            mediumItem = castedMediumLanguageList?.get(which).toString()

            vm.getSubjectListData(
                userid = prefs.user.userId,
                installid = prefs.installId.orEmpty(),
                userMedium = castedMediumLanguageList?.get(which).toString(),
                standard = binding.etStandard.text.toString()
            )
        }
    }*/

    private fun setSelectDropdown() {
        standardDialog = AlertDialog.Builder(this)
        standardDialog?.setItems(castedSelectStandardList) { _, which ->
            binding.etStandard.setText(castedSelectStandardList?.get(which))
            subjectItem = castedSelectStandardList?.get(which).toString()

            vm.getSubjectListData(
                userid = prefs.user.userId,
                installid = prefs.installId.orEmpty(),
                userMedium = binding.etModule.text.toString(),
                standard = castedSelectStandardList?.get(which).toString()
            )
        }
    }

    private fun setRcv() {
        BaseRvBindingAdapter(
            layoutId = R.layout.raw_add_demand,
            list = vm.subjectData,
            br = BR.data,
            clickListener = { v, t, p ->
            },
            viewHolder = { v, t, p ->
            }
        )

        rvUtil = RvUtil(
            adapter = editDemandAdapter,
            rv = binding.rvEditDemand,
        )
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is EditDemandDataResponse -> {
                        vm.subjectData.clear()

                        apiRenderState.result.data?.demand?.itemslist?.map {
                           // it?.mediumItem = binding.etModule.text.toString()
                            //vm.subjectData.add(it)
                           // localSubjectData.add(it)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}