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
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.esjayit.BR
import com.esjayit.BuildConfig
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.DEMAND_DATE
import com.esjayit.apnabazar.AppConstants.App.BundleData.EDIT_DEMAND_DATA
import com.esjayit.apnabazar.AppConstants.App.BundleData.FOR_EDIT_DEMAND
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.MiscUtil
import com.esjayit.apnabazar.helper.util.getDateString
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.ActivityAddDemandBinding
import com.google.android.material.datepicker.*
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
    var listData: ArrayList<AddDemandForAPINew>? = ArrayList()
    var isBtnEnable = true

    var subjectItem: String = ""
    var mediumItem: String = ""
    var searchKeyword: String = ""

    var subjectDataAdapter: BaseRvBindingAdapter<ItemlistItem?>? = null
    var rvUtil: RvUtil? = null
    val localSubjectData = mutableListOf<ItemlistItem?>()
    val savedList = mutableListOf<ItemlistItem?>()

    private var datePicker: MaterialDatePicker<Long>? = null
    var editProfileDataAdapter: BaseRvBindingAdapter<DemandItemslistItem?>? = null
    var editRvUtil: RvUtil? = null
    val localEditListData = mutableListOf<DemandItemslistItem?>()
    val editList = mutableListOf<DemandItemslistItem?>()

    var clickedPosition = -1
    private lateinit var debounceListener: (String) -> Unit
    var did: String? = null
    var demandDate: String? = null
    var isFromEditDemand = false
    var subectDemandData: List<DummyAddDemand>? = null
    var editDemandData: List<AddItemslistItem>? = null
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
                btnAddDemand.text = getString(R.string.txt_edit_demand)
            }
            progressDialog.showProgress()
        } else {
            addDemandRcv()
        }

        doSearch()
        progressDialog.showProgress()
        binding.tvNoData.visibility = View.GONE

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
                        localEditListData.clear()
                        binding.tvNoData.visibility = View.GONE
                        editProfileDataAdapter?.addData(
                            editList,
                            isClear = true
                        )
                        editRvUtil?.rvAdapter?.notifyDataSetChanged()
                    }
                } else {

                    if (searchText?.isEmpty() == true || searchText?.isBlank() == true) {
                        localSubjectData.clear()
                        binding.tvNoData.visibility = View.GONE
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
                            if (localEditListData.isNullOrEmpty()) {
                                binding.tvNoData.visibility = View.VISIBLE
                            }
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
                            if (localSubjectData.isNullOrEmpty()) {
                                binding.tvNoData.visibility = View.VISIBLE
                            }
                            rvUtil?.rvAdapter?.notifyDataSetChanged()
                        }
                    }
                    return@setOnEditorActionListener true
                }

                false
            }
    }

    private fun getCurrentDateTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

    private fun datePicker() {

        datePicker = MaterialDatePicker.Builder.datePicker().apply {
            setTitleText("")
            val date = Calendar.getInstance()

            val dateValidatorMax: CalendarConstraints.DateValidator =
                DateValidatorPointForward.now()

            val listValidators = ArrayList<CalendarConstraints.DateValidator>()
            listValidators.add(dateValidatorMax)

            val validators = CompositeDateValidator.allOf(listValidators)
            setCalendarConstraints(CalendarConstraints.Builder().setValidator(validators).build())
            setTheme(R.style.DialogTheme)
            setSelection(date.timeInMillis)
        }.build()

        datePicker?.addOnPositiveButtonClickListener(materialDateListener)
        datePicker?.show(supportFragmentManager, "")
    }

    private val materialDateListener: MaterialPickerOnPositiveButtonClickListener<Long?> =
        MaterialPickerOnPositiveButtonClickListener<Long?> {

            val dte = datePicker?.selection?.getDateString("dd")
            val mnt = datePicker?.selection?.getDateString("MM")
            val yar = datePicker?.selection?.getDateString("YYYY")

            val datTime = "$yar-$mnt-$dte "

            binding.etDate.setText(datTime)
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
                ),
                noDataViews = listOf(binding.tvNoData)
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
                    .doOnTextChanged { text, start, before, count ->
                        editProfileDataAdapter?.list?.get(p)?.qty = text.toString()
                    }
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
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
            binding.etModule -> {
                mediumDialog?.show()
            }
            binding.etStandard -> {
                standardDialog?.show()
            }
            binding.btnAddDemand -> {
                progressDialog.showProgress()

                //From EditDemand
                if (isFromEditDemand) {
                    var totalAmount = 0
                    var amt = 0

                    editProfileDataAdapter?.notifyDataSetChanged()

                    editDemandData =
                        editProfileDataAdapter?.list?.filter { it?.qty?.toInt()!! > 0 }?.map {
                            AddItemslistItem(
                                tranid = getIdOrEmptyStr(it?.id),
                                itemid = it?.itemid,
                                amount = it?.amount,
                                bunchqty = it?.bunchqty,
                                rate = it?.rate?.toFloat(),
                                qty = it?.qty
                            )
                        }

                    editDemandData?.forEachIndexed { index, itemlistItem ->
                        amt = (itemlistItem.rate?.roundToInt()
                            ?.let { itemlistItem.qty?.toInt()?.times(it) }!!)

                        totalAmount += (itemlistItem.qty?.toInt()
                            ?.times(itemlistItem.rate.toInt())!!)

                    }

                    vm.addEditDemand(
                        EditDemandDataVal(
                            demanddate = binding.etDate.text.toString(),
                            userid = prefs.user.userId,
                            demandid = did.orEmpty(),
                            totalamt = totalAmount.toString(),
                            packagename = BuildConfig.APPLICATION_ID,
                            versioncode = BuildConfig.VERSION_CODE.toString(),
                            installid = prefs.installId,
                            itemslist = editDemandData
                        )
                    )
                }
                else {
                    //From Add Demand
                    subectDemandData =
                        subjectDataAdapter?.list?.filter { it?.qty?.toInt()!! > 0 }?.map {
                            DummyAddDemand(
                                subjectName = it?.subname,
                                qty = it?.qty,
                                bunch = it?.thock,
                                standard = it?.standard,
                                itemId = it?.itemid,
                                rate = it?.itemrate?.toFloat(),
                                amount = getAmount(
                                    it?.itemrate?.toFloat(),
                                    it?.qty?.toInt()
                                ).toString()
                            )
                        }
                    if (subectDemandData != null) {
                        var totalAmount = 0
                        var amt = 0

                        AppConstants.App.itemlistItem.addAll(subectDemandData!!)
                        subectDemandData?.forEachIndexed { index, itemlistItem ->

                            amt = (itemlistItem.rate?.roundToInt()
                                ?.let { itemlistItem.qty?.toInt()?.times(it) }!!)

                            totalAmount += (itemlistItem.rate?.toInt()?.let {
                                itemlistItem.qty?.toInt()
                                    ?.times(it)
                            }!!)

                            listData?.add(
                                AddDemandForAPINew(
                                    itemid = itemlistItem.itemId,
                                    qty = itemlistItem.qty,
                                    rate = itemlistItem.rate.toString(),
                                    amount = ((itemlistItem.qty?.toInt()!! * itemlistItem.rate
                                        ?.roundToInt()!!).toString()),
                                    bunchqty = itemlistItem.bunch
                                )
                            )
                        }

                        // totalAmount = amt * totalQty

                        val addDemand = AddDemand(
                            demanddate = binding.etDate.text.toString(),
                            userid = prefs.user.userId,
                            totalamt = totalAmount.toString(),
                            itemslist = listData,
                            installid = prefs.installId.orEmpty()
                        )

                        vm.addDemandString(
                            addDemand
                        )
                    }
                }

                binding.btnAddDemand.isEnabled = false
            }
            binding.ivBack -> {
                finishAct()
            }
            binding.etDate -> {
                datePicker()
            }
        }
    }

    private fun getIdOrEmptyStr(id: String?): String? {
        return id?.ifEmpty {
            ""
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
                        progressDialog?.hideProgress()
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
                        "DEMAND DATA : ${apiRenderState.result}".logE()

                        if (apiRenderState.result.data?.itemlist?.isNullOrEmpty() == true) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE

                            apiRenderState.result.data?.itemlist?.map {
                                it?.mediumItem = binding.etModule.text.toString()
                                vm.subjectData.add(it)
                                savedList.add(it)
                            }

                            rvUtil?.rvAdapter?.notifyDataSetChanged()
                        }
                        progressDialog.hideProgress()
                    }
                    //For Add Demand
                    is CommonResponse -> {
                        //"Response: ${apiRenderState.result}".logE()
                        isBtnEnable = false
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
                            binding.btnAddDemand.isEnabled = true
                        }
                        progressDialog.hideProgress()
                    }
                    is EditDemandDataResponse -> {

                        "Response: data - ${apiRenderState.result.data}".logE()

                        vm.editDemandData.clear()

                        if (apiRenderState.result.data?.demand?.itemslist?.isEmpty() == true) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE

                            apiRenderState.result.data?.demand?.itemslist?.map {
                                vm.editDemandData.add(it)
                                editList.add(it)
                            }

                            editRvUtil?.rvAdapter?.notifyDataSetChanged()

                            editProfileDataAdapter?.list?.forEach {
                                l.add(it?.qty?.toInt())
                            }
                        }
                        progressDialog.hideProgress()
                    }
                    is SingleEditItemResponse -> {
                        if (clickedPos >= 0) {
                            rvUtil?.rvAdapter?.notifyItemChanged(clickedPosition)
                        }
                        progressDialog.hideProgress()
                    }
                }
            }
            ApiRenderState.Idle -> {
                progressDialog.hideProgress()
            }
            ApiRenderState.Loading -> {
                progressDialog.showProgress()
            }
            is ApiRenderState.ValidationError -> {
                progressDialog.hideProgress()
            }
            is ApiRenderState.ApiError<*> -> {
                isBtnEnable = true
                progressDialog.hideProgress()
                "Error API CALLING API ERROR".logE()
                //errorToast("Error Ocured")
            }
        }
    }
}
