package com.esjayit.apnabazar.main.dashboard.view.demand


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.GetDemandDataResponse
import com.esjayit.apnabazar.data.model.response.ItemlistItem
import com.esjayit.apnabazar.data.model.response.MediumResponse
import com.esjayit.apnabazar.data.model.response.StandardResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.ActivityAddDemandBinding
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class AddDemandAct : BaseAct<ActivityAddDemandBinding, DemandListVM>(Layouts.activity_add_demand) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }
    private var datePicker: MaterialDatePicker<Long>? = null

    var mediumDialog: AlertDialog.Builder? = null
    var standardDialog: AlertDialog.Builder? = null

    private var mediumLanguage = mutableListOf<String>()
    private var castedMediumLanguageList: Array<String>? = null

    private var selectStandard = mutableListOf<String>()
    private var castedSelectStandardList: Array<String>? = null

    var subjectItem: String = ""
    var mediumItem: String = ""

    lateinit var subjectData: BaseRvBindingAdapter<ItemlistItem?>
    var rvUtil: RvUtil? = null

    override fun init() {
        vm.apply {
            getMediumList(userid = prefs.user.userId, installid = prefs.installId.orEmpty())
        }

        setSelectDropdown()
        getCurrentDateTime()

        progressDialog.showProgress()
    }

    private fun getCurrentDateTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm a")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

    private fun setRecyclerView() {
        subjectData = BaseRvBindingAdapter(
            list = mutableListOf(),
            br = BR.data,
            clickListener = { v, t, p ->

            },
        )

        rvUtil = RvUtil(
            adapter = subjectData,
            rv = binding.rvSubjectData,
            decoration = RvItemDecoration.buildDecoration(this, R.dimen._8sdp),
        )
    }

    private fun setMediumDropdown() {
        mediumDialog = AlertDialog.Builder(this)
        mediumDialog?.setItems(castedMediumLanguageList) { _, which ->
            binding.etModule.setText(castedMediumLanguageList?.get(which))
            mediumItem = castedMediumLanguageList?.get(which).toString()
        }
    }

    private fun setSelectDropdown() {
        standardDialog = AlertDialog.Builder(this)
        standardDialog?.setItems(castedSelectStandardList) { _, which ->
            binding.etStandard.setText(castedSelectStandardList?.get(which))
            subjectItem = castedSelectStandardList?.get(which).toString()
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.etModule -> {
                mediumDialog?.show()
            }
            binding.etStandard -> {
                standardDialog?.show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is MediumResponse -> {
                        castedMediumLanguageList = emptyArray()
                        mediumLanguage.clear()
                        apiRenderState.result.data?.mediumlist?.forEachIndexed { index, mediumlistItem ->
                            mediumlistItem?.value?.let { mediumLanguage.add(index, it) }
                        }
                        castedMediumLanguageList = mediumLanguage.toTypedArray()
                        setMediumDropdown()
                        binding.etModule.setText(castedMediumLanguageList?.get(0))

                        vm.getStandard(
                            userid = prefs.user.userId,
                            userMedium = castedMediumLanguageList?.get(0).orEmpty(),
                            installid = prefs.user.installId
                        )
                    }
                    is StandardResponse -> {
                        castedSelectStandardList = emptyArray()
                        selectStandard.clear()
                        apiRenderState.result.data?.stdlist?.forEachIndexed { index, stdlist ->
                            stdlist?.std?.let { selectStandard.add(index, it) }
                        }
                        castedSelectStandardList = selectStandard.toTypedArray()
                        setSelectDropdown()
                        binding.etStandard.setText(castedSelectStandardList?.get(0))

                        castedSelectStandardList?.get(0)?.let {
                            vm.getSubjectListData(
                                userid = prefs.user.userId,
                                installid = prefs.installId.orEmpty(),
                                userMedium = binding.etModule.text.toString(),
                                standard = it
                            )
                        }
                    }
                    is GetDemandDataResponse -> {
                        "Response: data - ${apiRenderState.result.data}".logE()
                        setRecyclerView()

                        //Add data to table
                        apiRenderState.result.data?.itemlist?.let {
                            subjectData.addData(
                                it,
                                isClear = true
                            )
                        }
                        subjectData.notifyDataSetChanged()
                        progressDialog.hideProgress()
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                // showProgress()
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