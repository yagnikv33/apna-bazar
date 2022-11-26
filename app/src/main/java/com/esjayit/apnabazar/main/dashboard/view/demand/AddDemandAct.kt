package com.esjayit.apnabazar.main.dashboard.view.demand


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.ADD_DEMAND
import com.esjayit.apnabazar.AppConstants.Communication.BundleData.DEMAND_LIST_ITEMS
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.MiscUtil
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.ActivityAddDemandBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class AddDemandAct : BaseAct<ActivityAddDemandBinding, DemandListVM>(Layouts.activity_add_demand) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    var mediumDialog: AlertDialog.Builder? = null
    var standardDialog: AlertDialog.Builder? = null

    private var mediumLanguage = mutableListOf<String>()
    private var castedMediumLanguageList: Array<String>? = null

    private var selectStandard = mutableListOf<String>()
    private var castedSelectStandardList: Array<String>? = null


    var subjectItem: String = ""
    var mediumItem: String = ""
    var searchKeyword: String = ""
    val localSubjectData = mutableListOf<ItemlistItem?>()

    lateinit var subjectDataAdapter: BaseRvBindingAdapter<ItemlistItem?>
    var rvUtil: RvUtil? = null
    var clickedPosition = -1
    private lateinit var debounceListener: (String) -> Unit
    private val addDemandList = hashSetOf<DummyAddDemand>()
    private lateinit var debounceListenerForRcv: (String) -> Unit

    override fun init() {
        vm.apply {
            getMediumList(userid = prefs.user.userId, installid = prefs.installId.orEmpty())
        }

        setSelectDropdown()
        getCurrentDateTime()
        setRecyclerView()
        doSearch()
        progressDialog.showProgress()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doSearch() {
        debounceListener =
            MiscUtil.throttleLatest(scope = lifecycleScope, intervalMs = 500L) { searchText ->
                if (searchText.length > 1) {
                    if (searchText == vm.subjectData[2]?.subname) {
                        vm.subjectData.map {
                            localSubjectData.add(it)
                        }
                        subjectDataAdapter.addData(localSubjectData, isClear = true)
                        rvUtil?.rvAdapter?.notifyDataSetChanged()
                    }
                }
            }

        /** Key Board OnTextChangeListener */
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(searchText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchKeyword = searchText.toString()
                localSubjectData.filter {
                    "Search data: $searchText + ${it?.subname.toString()}".logE()
                    searchText!!.contains(it?.subname.toString())
                }.let {
                    subjectDataAdapter.addData(it, isClear = true)
                    "Filterd Data: $it".logE()
//                    rvUtil?.rvAdapter?.notifyDataSetChanged()
                }
                rvUtil?.rvAdapter?.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun getCurrentDateTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm a")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

    private fun setRecyclerView() {
        subjectDataAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_add_demand,
            list = vm.subjectData,
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
                    R.id.edt_qty -> {
                        "CLICK tab".logE()
                    }
                }
            },
            viewHolder = { v, t, p ->

                /** Text Change */
                /* v.findViewById<EditText>(R.id.edt_qty).addTextChangedListener(object : TextWatcher {
                     override fun beforeTextChanged(
                         s: CharSequence?,
                         start: Int,
                         count: Int,
                         after: Int
                     ) {
                     }

                     override fun onTextChanged(
                         s: CharSequence?,
                         start: Int,
                         before: Int,
                         count: Int
                     ) {

                     }

                     override fun afterTextChanged(s: Editable?) {
 //                        addDemandList.add(
 //                            DummyAddDemand(
 //                                subjectName = t?.subname.orEmpty(),
 //                                qty = s.toString(),
 //                                bunch = t?.thock,
 //                                standard = t?.standard
 //                            )
 //                        )
                         debounceListener.invoke(s.toString())
                         //"click Items: afterTextChanged $s".logE()
                     }
                 })

                 debounceListener =
                     MiscUtil.throttleLatest(
                         scope = lifecycleScope,
                         intervalMs = 500L
                     ) { qty ->

                         "click Items: DEBOUNCED!!!!".logE()

                         if (qty.length > 1) {
                             addDemandList.add(
                                 DummyAddDemand(
                                     subjectName = t?.subname.orEmpty(),
                                     qty = qty,
                                     bunch = t?.thock,
                                     standard = t?.standard
                                 )
                             )

                             "click Items: SIZE - ${addDemandList.size}".logE()
                         }
                     }*/

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

        rvUtil = RvUtil(
            adapter = subjectDataAdapter,
            rv = binding.rvSubjectData,
            decoration = RvItemDecoration.buildDecoration(this, R.dimen._8sdp),
        )
    }

    private fun setMediumDropdown() {
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
    }

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

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.etModule -> {
                mediumDialog?.show()
            }
            binding.etStandard -> {
                standardDialog?.show()
            }
            binding.btnAddDemand -> {

                val productNameList = subjectDataAdapter.list.filter { it?.qty?.toInt()!! > 0 }.map {
                        DummyAddDemand(
                            subjectName = it?.subname,
                            qty = it?.qty,
                            bunch = it?.thock,
                            standard = it?.standard,
                            itemId = it?.itemid,
                            rate = it?.itemrate,
                            amount = getAmount(it?.itemrate?.toFloat(), it?.qty?.toInt()).toString()
                        )
                    }

                "Data List: OBJECT $productNameList".logE()

                AppConstants.App.itemlistItem.addAll(productNameList)
                startActivity(ViewDemandAct::class.java)


            }
        }
    }

    private fun getAmount(rate: Float?, qty: Int?): Int {
        if (rate != null) {
            return rate.roundToInt() * qty!!
        }
        return 0
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
                        mediumItem = castedMediumLanguageList?.get(0).toString()

                        vm.getStandard(
                            userid = prefs.user.userId,
                            userMedium = castedMediumLanguageList?.get(0).orEmpty(),
                            installid = prefs.user.installId
                        )

                        subjectDataAdapter.list.map {
                            it?.mediumItem = castedMediumLanguageList?.get(0).orEmpty()
                        }
                        if (clickedPosition != -1) {
                            rvUtil?.rvAdapter?.notifyItemChanged(clickedPosition)
                        }
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
                        subjectItem = castedSelectStandardList?.get(0).toString()

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

                        vm.subjectData.clear()

                        apiRenderState.result.data?.itemlist?.map {
                            it?.mediumItem = binding.etModule.text.toString()
                            vm.subjectData.add(it)
                            localSubjectData.add(it)
                        }

                        //Add data to table
//                        apiRenderState.result.data?.itemlist?.let {
//                            subjectData.addData(
//                                it,
//                                isClear = true
//                            )
//                        }
                        rvUtil?.rvAdapter?.notifyDataSetChanged()
                        //subjectData.notifyDataSetChanged()
                        progressDialog.hideProgress()
                    }

                    //For Add Demand
                    is CommonResponse -> {
                        successToast(apiRenderState.result.message.toString())
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