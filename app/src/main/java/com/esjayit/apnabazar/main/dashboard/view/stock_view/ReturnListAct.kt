package com.esjayit.apnabazar.main.dashboard.view.stock_view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.App.BundleData.BILL_DATE
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.*
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.getDateString
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.ActivityReturnListBinding
import com.google.android.material.datepicker.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class ReturnListAct :
    BaseAct<ActivityReturnListBinding, StockViewVM>(Layouts.activity_return_list) {

    override val vm: StockViewVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    var mediumDialog: AlertDialog.Builder? = null
    var standardDialog: AlertDialog.Builder? = null

    private var mediumLanguage = mutableListOf<String>()
    private var castedMediumLanguageList: Array<String>? = null

    private var selectStandard = mutableListOf<String>()
    private var castedSelectStandardList: Array<String>? = null
    private var datePicker: MaterialDatePicker<Long>? = null

    var subjectItem: String = ""
    var mediumItem: String = ""

    var returnBookAdapter: BaseRvBindingAdapter<ReturnitemsItem?>? = null
    var rvUtil: RvUtil? = null

    var clickedPosition = -1
    private var addReturnBook: List<AddRetutranlistItem>? = null
    var did: String? = null
    var demandDate: String? = null
    var billDate: String? = null
    var demandNo: Int? = null
    var isFromEditDemand = false
    var totalBill = 0
    var isExpandable = true

    override fun init() {

        val bundle = intent.extras

        billDate = bundle?.getString(BILL_DATE).toString()

        vm.apply {
            getMediumList(userid = prefs.user.userId, installid = prefs.installId.orEmpty())
        }
        setSelectDropdown()
        getCurrentDateTime()

        setRv()

        progressDialog.showProgress()

        binding.tvNoData.visibility = View.GONE
    }

    var currentClickItem: ReturnitemsItem? = null
    private fun setRv() {
        returnBookAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_return,
            list = vm.returnDataList,
            br = BR.data,
            clickListener = { v, item, p ->

                if (isExpandable) {
                    item?.returnItemResponse?.let {
                        // notify data

                        when (v.id) {
                            R.id.ll_sub_header, R.id.tv_subject_sub_header, R.id.tv_rate, R.id.tv_standard_sub_header, R.id.main_view -> {
                                vm.returnDataList.forEach {
                                    it?.isTextVisible = false
                                }
                                item.isTextVisible = !item.isTextVisible

                                //Calculating bill
                                totalBill = item.buyqty?.toInt()
                                    ?.let { it1 ->
                                        item.rate?.roundToInt()?.let { it2 ->
                                            getCurrentBill(
                                                rate = it2,
                                                qty = it1
                                            )
                                        }
                                    } ?: 0

                                rvUtil?.notifyAdapter()
                            }
                            else -> {}
                        }
                    } ?: run {
                        vm.getReturnSingleItem(
                            userid = prefs.user.userId,
                            installid = prefs.installId.orEmpty(),
                            itemId = item?.itemid.orEmpty()
                        )
                        progressDialog.showProgress()
                        currentClickItem = item
                    }
                }
                isExpandable = true
            },
            viewHolder = { v, t, p ->

                /** Ime DONE Action */
                v.findViewById<EditText>(R.id.edt_qty)
                    .setOnEditorActionListener { v, actionId, event ->

                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (v.text.toString()
                                    .toInt() > (t?.maxretu?.toInt() ?: 0)
                            ) {
                                errorToast("Return quantity must smaller than max qty")
                                v.text = ""
                                isExpandable = false
                            }
                            v.hideSoftKeyboard()
                            return@setOnEditorActionListener true
                        }

                        false
                    }
            }
        )

        rvUtil = returnBookAdapter?.let {
            RvUtil(
                adapter = it,
                rv = binding.rvReturnList,
                decoration = RvItemDecoration.buildDecoration(
                    this,
                    R.dimen._1sdp,
                    color = R.color.grey
                ),
            )
        }
    }

    private fun datePicker() {

        datePicker = MaterialDatePicker.Builder.datePicker().apply {
            setTitleText("")
            val date = Calendar.getInstance()
            val dateValidatorMax: CalendarConstraints.DateValidator =
                DateValidatorPointBackward.before(date.timeInMillis)
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

    private fun setMediumDropdown() {
        mediumDialog = AlertDialog.Builder(this)
        mediumDialog?.setItems(castedMediumLanguageList) { _, which ->
            binding.etModule.setText(castedMediumLanguageList?.get(which))
            mediumItem = castedMediumLanguageList?.get(which).toString()

            if (!isFromEditDemand) {
                vm.getReturnItemListing(
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

            vm.getReturnItemListing(
                userid = prefs.user.userId,
                installid = prefs.installId.orEmpty(),
                userMedium = binding.etModule.text.toString(),
                standard = castedSelectStandardList?.get(which).toString()
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
            binding.etModule -> {
                mediumDialog?.show()
            }
            binding.etStandard -> {
                standardDialog?.show()
            }
            binding.btnAddReturn -> {

                binding.btnAddReturn.isEnabled = false

                if (returnBookAdapter?.list?.all {
                        (it?.retuqty?.toIntOrNull() ?: 0) <= it?.maxretu?.toInt()!!
                    } == true) {

                    addReturnBook =
                        returnBookAdapter?.list?.filter { (it?.retuqty?.toIntOrNull() ?: 0) > 0 }
                            ?.map {
                                AddRetutranlistItem(
                                    itemid = it?.itemid,
                                    buyqty = it?.buyqty,
                                    maxretu = it?.maxretu,
                                    retuqty = it?.retuqty,
                                    rate = it?.rate.toString()
                                )
                            }

                    vm.addReturnBookDataVal(
                        addReturnBook = AddReturnBook(
                            userid = prefs.user.userId,
                            installid = prefs.installId,
                            billamount = totalBill.toString(),
                            billdate = binding.etDate.text.toString(),
                            retutranlist = addReturnBook
                        )
                    )

                } else {
                    errorToast("Return quantity must smaller than max qty")
                }


//                addReturnBook?.map {
//                    vm.addReturnBook(
//                        userid = prefs.user.userId,
//                        installid = prefs.installId.orEmpty(),
//                        billAmount = totalBill.toString(),
//                        billDate = binding.etDate.text.toString(),
//                        returnList = addReturnBook!!.toTypedArray()
//                    )
//                }
            }
            binding.ivBack -> {
                finishAct()
            }
            binding.etDate -> {
                datePicker()
            }
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
        }
    }

    private fun getCurrentBill(rate: Int?, qty: Int?): Int {
        if (rate == 0) {
            return 0
        }
        if (qty == 0) {
            return 0
        }
        if (rate != null) {
            return rate * qty!!
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
                            vm.getReturnItemListing(
                                userid = prefs.user.userId,
                                standard = it,
                                userMedium = binding.etModule.text.toString(),
                                installid = prefs.installId.orEmpty(),
                            )
                        }
                    }
                    is GetReturnItemListResponse -> {

                        vm.returnList.clear()

                        if (apiRenderState.result.data?.returnitems.isNullOrEmpty()) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE

                            apiRenderState.result.data?.returnitems?.map {
                                vm.returnDataList.add(it)
                            }
                            //"Response: return - ${apiRenderState.result}".logE()
                            rvUtil?.rvAdapter?.notifyDataSetChanged()
                        }

                        progressDialog.hideProgress()
                    }

                    is CommonResponse -> {

                        successToast(apiRenderState.result.message.toString(), callback = {
                            val returnIntent = Intent()
                            setResult(RESULT_OK, returnIntent)
                            finishAct()
                        })
                        binding.btnAddReturn.isEnabled = true
                        progressDialog.hideProgress()
                    }

                    is SingleItemResponse -> {
                        //"Response: GetReturnSingleDetailResponse - ${apiRenderState.result.data}".logE()
                        currentClickItem?.returnItemResponse =
                            apiRenderState.result.data?.returnitems
                        returnBookAdapter?.list?.forEach {
                            it?.isTextVisible = false
                        }
                        currentClickItem?.isTextVisible = true
                        returnBookAdapter?.notifyDataSetChanged()
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
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                progressDialog.hideProgress()
                "Error API CALLING API ERROR".logE()
            }
        }
    }
}