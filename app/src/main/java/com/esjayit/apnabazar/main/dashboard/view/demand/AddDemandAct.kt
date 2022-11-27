package com.esjayit.apnabazar.main.dashboard.view.demand


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_DATE
import com.esjayit.apnabazar.AppConstants.App.BundleData.EDIT_DEMAND_DATA
import com.esjayit.apnabazar.AppConstants.App.BundleData.FOR_EDIT_DEMAND
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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.raw_demand_list.*
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
    var listData: ArrayList<AddDemandForAPI>? = ArrayList()

    var subjectItem: String = ""
    var mediumItem: String = ""
    var searchKeyword: String = ""

    var subjectDataAdapter: BaseRvBindingAdapter<ItemlistItem?>? = null
    var rvUtil: RvUtil? = null
    val localSubjectData = mutableListOf<ItemlistItem?>()
    val savedList = mutableListOf<ItemlistItem?>()

    var editProfileDataAdapter: BaseRvBindingAdapter<ItemslistItem?>? = null
    var editRvUtil: RvUtil? = null
    val localEditListData = mutableListOf<ItemslistItem?>()
    val editList = mutableListOf<ItemslistItem?>()

    var clickedPosition = -1
    private lateinit var debounceListener: (String) -> Unit
    var did: String? = null
    var demandDate: String? = null
    var isFromEditDemand = false
    var subectDemandData: List<DummyAddDemand>? = null
    var editDemandData: List<DummyEditDemand>? = null
    var clickedPos = -1
    val l = mutableListOf<Int?>()

    override fun init() {

        val bundel = intent?.extras
        did = bundel?.getString(EDIT_DEMAND_DATA)
        demandDate = bundel?.getString(DEMAND_DATE)
        isFromEditDemand = bundel?.getBoolean(FOR_EDIT_DEMAND) == true

        vm.apply {
            getMediumList(userid = prefs.user.userId, installid = prefs.installId.orEmpty())
        }

        setSelectDropdown()
        getCurrentDateTime()
        if (isFromEditDemand) {
            editDemandRcv()
            binding.apply {
                tvHeader.text = getString(R.string.txt_edit_demand)
            }
            progressDialog.showProgress()
        } else {
            addDemandRcv()
        }

        doSearch()
        progressDialog.showProgress()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doSearch() {
        debounceListener =
            MiscUtil.throttleLatest(scope = lifecycleScope, intervalMs = 1000L) { searchText ->

                "Searched: $searchText".logE()

                if (searchText.isNotEmpty()) {
                    if (isFromEditDemand) {
                        editProfileDataAdapter?.list?.forEach {
                            if (it?.subname?.toLowerCase(Locale.ENGLISH)
                                    ?.contains(searchText) == true
                            ) {
                                localEditListData.add(it)
                            }
                        }
                        editProfileDataAdapter?.addData(localEditListData, isClear = true)
                    } else {
                        subjectDataAdapter?.list?.forEach {
                            if (it?.subname?.toLowerCase(Locale.ENGLISH)
                                    ?.contains(searchText) == true
                            ) {
                                localSubjectData.add(it)
                            }
                        }
                        subjectDataAdapter?.addData(localSubjectData, isClear = true)
                    }
                    rvUtil?.rvAdapter?.notifyDataSetChanged()
                }
            }

        /** Key Board OnTextChangeListener */
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(searchText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchKeyword = searchText.toString()

                if (isFromEditDemand) {

                    if (searchText?.isEmpty() == true || searchText?.isBlank() == true) {
                        editProfileDataAdapter?.addData(
                            editList,
                            isClear = true
                        )
                        editRvUtil?.rvAdapter?.notifyDataSetChanged()
                    }
                } else {

                    if (searchText?.isEmpty() == true || searchText?.isBlank() == true) {
                        subjectDataAdapter?.addData(
                            savedList,
                            isClear = true
                        )
                        rvUtil?.rvAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        /** Ime DONE Action */
        binding.edSearch
            .setOnEditorActionListener { v, actionId, event ->

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    v.hideSoftKeyboard()

                    if (searchKeyword.isNotEmpty()) {
                        if (isFromEditDemand) {
                            editProfileDataAdapter?.list?.forEach {
                                if (it?.subname?.toLowerCase(Locale.ENGLISH)
                                        ?.contains(searchKeyword) == true
                                ) {
                                    localEditListData.add(it)
                                }
                            }
                            editProfileDataAdapter?.addData(localEditListData, isClear = true)
                            editRvUtil?.rvAdapter?.notifyDataSetChanged()
                        } else {
                            subjectDataAdapter?.list?.forEach {
                                if (it?.subname?.toLowerCase(Locale.ENGLISH)
                                        ?.contains(searchKeyword) == true
                                ) {
                                    localSubjectData.add(it)
                                }
                            }
                            subjectDataAdapter?.addData(localSubjectData, isClear = true)
                            rvUtil?.rvAdapter?.notifyDataSetChanged()
                        }
                    }
                    return@setOnEditorActionListener true
                }

                false
            }
    }

    private fun getCurrentDateTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm a")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

    private fun addDemandRcv() {
        subjectDataAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_add_demand,
            list = vm.subjectData,
            br = BR.data,
            clickListener = { v, t, p ->
                when (v.id) {
                    R.id.main_view, R.id.ll_sub_header, R.id.tv_subject_sub_header, R.id.tv_rate, R.id.tv_standard_sub_header -> {
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

        rvUtil = subjectDataAdapter?.let {
            RvUtil(
                adapter = it,
                rv = binding.rvSubjectData,
                decoration = RvItemDecoration.buildDecoration(
                    this,
                    R.dimen._1sdp,
                    color = R.color.grey
                )
            )
        }
    }

    private fun editDemandRcv() {
        editProfileDataAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_edit_demand,
            list = vm.editDemandData,
            br = BR.data,
            clickListener = { v, t, p ->

                when (v.id) {
                    R.id.main_view, R.id.ll_sub_header, R.id.tv_subject_sub_header, R.id.tv_rate, R.id.tv_standard_sub_header -> {
                        clickedPos = p
                        vm.getSingleEditItemDetail(
                            userid = prefs.user.userId,
                            itemid = t?.itemid.orEmpty(),
                            installid = prefs.installId.orEmpty()
                        )

                        vm.editDemandData.forEach {
                            it?.isTextVisible = false
                        }

                        t?.isTextVisible = !t?.isTextVisible!!

                        editRvUtil?.notifyAdapter()
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

        editRvUtil = editProfileDataAdapter?.let {
            RvUtil(
                adapter = it,
                rv = binding.rvSubjectData,
                decoration = RvItemDecoration.buildDecoration(this, R.dimen._1sdp, R.color.grey),
            )
        }
    }

    private fun setMediumDropdown() {
        mediumDialog = AlertDialog.Builder(this)
        mediumDialog?.setItems(castedMediumLanguageList) { _, which ->
            binding.etModule.setText(castedMediumLanguageList?.get(which))
            mediumItem = castedMediumLanguageList?.get(which).toString()

            if (!isFromEditDemand) {
                vm.getSubjectListData(
                    userid = prefs.user.userId,
                    installid = prefs.installId.orEmpty(),
                    userMedium = castedMediumLanguageList?.get(which).toString(),
                    standard = binding.etStandard.text.toString()
                )
            }
        }
    }

    private fun setSelectDropdown() {
        standardDialog = AlertDialog.Builder(this)
        standardDialog?.setItems(castedSelectStandardList) { _, which ->
            binding.etStandard.setText(castedSelectStandardList?.get(which))
            subjectItem = castedSelectStandardList?.get(which).toString()

            if (!isFromEditDemand) {
                vm.getSubjectListData(
                    userid = prefs.user.userId,
                    installid = prefs.installId.orEmpty(),
                    userMedium = binding.etModule.text.toString(),
                    standard = castedSelectStandardList?.get(which).toString()
                )
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
                progressDialog.showProgress()

                //From EditDemand

                editDemandData =
                    editProfileDataAdapter?.list?.filter { it?.qty?.toInt()!! > 0 }?.map {
                        DummyEditDemand(
                            itemid = it?.itemid,
                            qty = it?.qty,
                            rate = it?.rate,
                            amount = it?.amount,
                            bunchqty = it?.bunchqty
                        )
                    }

                if (editDemandData != null) {
                    val totalAmount: Int = 0

                    editDemandData?.toTypedArray()?.let {
                        vm.editDemand(
                            demanddate = binding.etDate.text.toString(),
                            userid = prefs.user.userId,
                            totalamt = totalAmount.toString(),
                            installid = prefs.installId.orEmpty(),
                            itemslist = it,
                            demandid = did.orEmpty()
                        )
                    }

                } else {
                    //From Add Demand
                    subectDemandData =
                        subjectDataAdapter?.list?.filter { it?.qty?.toInt()!! > 0 }?.map {
                            DummyAddDemand(
                                subjectName = it?.subname,
                                qty = it?.qty,
                                bunch = it?.thock,
                                standard = it?.standard,
                                itemId = it?.itemid,
                                rate = it?.itemrate,
                                amount = getAmount(
                                    it?.itemrate?.toFloat(),
                                    it?.qty?.toInt()
                                ).toString()
                            )
                        }
                    if (subectDemandData != null) {
                        var totalQty = 0
                        val totalAmount: Int
                        var amt = 0

                        AppConstants.App.itemlistItem.addAll(subectDemandData!!)
                        subectDemandData?.forEachIndexed { index, itemlistItem ->
                            totalQty = itemlistItem.qty?.toInt()!! + itemlistItem.qty?.toInt()!!
                            amt = itemlistItem.rate?.toFloat()?.roundToInt()
                                ?.times(itemlistItem.qty?.toInt()!!)!!

                            listData?.add(
                                AddDemandForAPI(
                                    itemid = itemlistItem.itemId,
                                    qty = itemlistItem.qty,
                                    rate = itemlistItem.rate,
                                    amount = ((itemlistItem.qty?.toInt()!! * itemlistItem.rate?.toFloat()
                                        ?.roundToInt()!!).toString()),
                                    bunchqty = itemlistItem.bunch
                                )
                            )
                        }

                        totalAmount = amt * totalQty

                        vm.addDemandString(
                            demanddate = binding.etDate.text.toString(),
                            userid = prefs.user.userId,
                            totalamt = totalAmount.toString(),
                            installid = prefs.installId.orEmpty(),
                            itemslist = Gson().toJson(listData)
                        )
                    }
                }
            }
            binding.ivBack -> {
                finishAct()
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

                        subjectDataAdapter?.list?.map {
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

                        if (isFromEditDemand) {
                            vm.getEditDemandData(
                                userid = prefs.user.userId,
                                demandid = did.orEmpty(),
                                installid = prefs.installId.orEmpty()
                            )
                        } else {
                            castedSelectStandardList?.get(0)?.let {
                                vm.getSubjectListData(
                                    userid = prefs.user.userId,
                                    installid = prefs.installId.orEmpty(),
                                    userMedium = binding.etModule.text.toString(),
                                    standard = it
                                )
                            }
                        }

                    }
                    is GetDemandDataResponse -> {
                        vm.subjectData.clear()
                        "DEMAND DATA : ${apiRenderState.result.data}".logE()
                        apiRenderState.result.data?.itemlist?.map {
                            it?.mediumItem = binding.etModule.text.toString()
                            vm.subjectData.add(it)
                            savedList.add(it)
                        }

                        rvUtil?.rvAdapter?.notifyDataSetChanged()
                        progressDialog.hideProgress()
                    }
                    //For Add Demand
                    is CommonResponse -> {

                        "Response: ${apiRenderState.result}".logE()
                        val statusCode = apiRenderState.result.statusCode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            successToast(apiRenderState.result.message.toString(), callback = {
                                if (it) {
                                    val returnIntent = Intent()
                                    setResult(RESULT_OK, returnIntent)
                                    finishAct()
                                }
                            })
                        } else {
                            errorToast(apiRenderState.result.message!!)
                        }
                    }
                    is EditDemandDataResponse -> {

                        vm.editDemandData.clear()

                        apiRenderState.result.data?.demand?.itemslist?.map {
                            vm.editDemandData.add(it)
                            editList.add(it)
                        }

                        editRvUtil?.rvAdapter?.notifyDataSetChanged()

                        editProfileDataAdapter?.list?.forEach {
                            l.add(it?.qty?.toInt())
                        }

                        progressDialog.hideProgress()
                    }
                    is SingleEditItemResponse -> {
                        if (clickedPos >= 0) {
                            rvUtil?.rvAdapter?.notifyItemChanged(clickedPosition)
                        }
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                progressDialog.hideProgress()
                "Error API CALLING API ERROR".logE()
                //errorToast("Error Ocured")
            }
        }
    }
}