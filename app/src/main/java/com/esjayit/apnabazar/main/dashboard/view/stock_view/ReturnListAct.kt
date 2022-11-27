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
import com.esjayit.apnabazar.helper.util.hideSoftKeyboard
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.databinding.ActivityReturnListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


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

    var subjectItem: String = ""
    var mediumItem: String = ""

    var returnBookAdapter: BaseRvBindingAdapter<ReturnitemsItem?>? = null
    var rvUtil: RvUtil? = null

    var clickedPosition = -1
    private var addReturnBook: List<DummyReturn>? = null
    var did: String? = null
    var demandDate: String? = null
    var billDate: String? = null
    var demandNo: Int? = null
    var isFromEditDemand = false

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
    }

    private fun setRv() {
        returnBookAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_return,
            list = vm.returnDataList,
            br = BR.data,
            clickListener = { v, t, p ->
                when (v.id) {
                    R.id.ll_sub_header, R.id.tv_subject_sub_header, R.id.tv_rate, R.id.tv_standard_sub_header -> {
                        vm.returnDataList.forEach {
                            it?.isTextVisible = false
                        }

                        t?.isTextVisible = !t?.isTextVisible!!

                        vm.getReturnSingleItem(userid = prefs.user.userId!!, installid = prefs.installId.orEmpty(), itemId = t?.itemid.orEmpty())
                        rvUtil?.notifyAdapter()
                    }
                    R.id.main_view -> {
                        vm.returnDataList.forEach {
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

        rvUtil = returnBookAdapter?.let {
            RvUtil(
                adapter = it,
                rv = binding.rvReturnList,
                decoration = RvItemDecoration.buildDecoration(this, R.dimen._1sdp, color = R.color.grey),
            )
        }
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
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm a")
        val currentDateandTime: String = sdf.format(Date())
        binding.etDate.setText(currentDateandTime)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnAddReturn -> {

                /* addReturnBook =
                     returnBookAdapter?.list?.filter { it?.retuqty?.toInt()!! > 0 }?.map {
                         DummyReturn(
                             itemid = it?.itemid,
                             buyqty = it?.buyqty?.toInt(),
                             maxretu = it?.maxretu?.toInt(),
                             retuqty = it?.retuqty?.toInt(),
                             rate = it?.rate?.toFloat()
                         )
                     }*/

                //TEMP
                val r: Array<DummyReturn> = Array(1, init = {
                    DummyReturn(
                        itemid = "9EB3989A-A23A-4436-9507-AC735DEBC163",
                        buyqty = 20,
                        maxretu = 32,
                        retuqty = 10,
                        rate = 60.00f
                    )
                })

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                "addReturn: ${r.size}".logE()

                addReturnBook?.map {
                    vm.addReturnBook(
                        userid = prefs.user.userId,
                        installid = prefs.installId.orEmpty(),
                        billAmount = "4323",
                        billDate = "2021-10-11",
                        returnList = r
                    )
                }
            }
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

//                        returnBookAdapter?.list?.map {
//                            it?.subname = castedMediumLanguageList?.get(0).orEmpty()
//                        }
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
//                        for (i in 1..15) {
//                            vm.returnDataList.add(
//                                ReturnitemsItem(
//                                    itemid = "123",
//                                    buyqty = "15",
//                                    rate = "100",
//                                    maxretu = "5",
//                                    subname = "MARIGOLD",
//                                    subcode = "1",
//                                    retuqty = "12",
//                                    true
//                                )
//                            )
//                        }
                        apiRenderState.result.data?.returnitems?.map {
                            vm.returnDataList.add(it)
                        }
                        "Response: return - ${apiRenderState.result}".logE()
                        rvUtil?.rvAdapter?.notifyDataSetChanged()
                        progressDialog.hideProgress()
                    }

                    is CommonResponse -> {
                        "Response: ${apiRenderState.result}".logE()

                        //finishAct()
                    }
                }
            }
            else -> {
                progressDialog.hideProgress()
            }
        }
    }
}