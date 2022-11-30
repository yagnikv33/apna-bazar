package com.esjayit.apnabazar.main.dashboard.view.stock_view

import android.annotation.SuppressLint
import android.view.View
import com.esjayit.BR
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants.App.BundleData.BILL_NO
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_DATE
import com.esjayit.apnabazar.AppConstants.App.BundleData.RETURN_ID
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.RetunlistItem
import com.esjayit.apnabazar.data.model.response.RetutranlistItem
import com.esjayit.apnabazar.data.model.response.ViewBookReturnDataResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.getDateString
import com.esjayit.apnabazar.helper.util.rvutil.RvItemDecoration
import com.esjayit.apnabazar.helper.util.rvutil.RvUtil
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.ActivityViewReturnBinding
import com.google.android.material.datepicker.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class ViewReturnAct :
    BaseAct<ActivityViewReturnBinding, StockViewVM>(Layouts.activity_view_return) {

    override val vm: StockViewVM by viewModel()
    override val hasProgress: Boolean = false
    val progressDialog: CustomProgress by lazy { CustomProgress(this) }

    var viewReturnAdapter: BaseRvBindingAdapter<RetutranlistItem>? = null

    private var datePicker: MaterialDatePicker<Long>? = null
    var rvUtil: RvUtil? = null
    var returnId = ""
    var returnDate = ""
    var billNo = ""
    var returnModel: RetunlistItem? = null

    override fun init() {

        val bundle = intent?.extras

        returnId = bundle?.getString(RETURN_ID).toString()
        returnDate = bundle?.getString(RETURN_DATE).toString()
        billNo = bundle?.getString(BILL_NO).toString()

        progressDialog.showProgress()

        binding.etDemandNo.setText(billNo)
        binding.etDate.setText(returnDate)

        //Api Call
        vm.viewReturnBook(
            userid = prefs.user.userId,
            installid = prefs.installId.orEmpty(),
            returnid = returnId
        )

        setRv()

        binding.tvNoData.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    private fun setRv() {
        viewReturnAdapter = BaseRvBindingAdapter(
            layoutId = R.layout.raw_view_return,
            list = vm.viewReturnList,
            br = BR.data
        )

        rvUtil = RvUtil(
            adapter = viewReturnAdapter!!,
            rv = binding.rvViewReturn,
            decoration = RvItemDecoration.buildDecoration(this, R.dimen._1sdp, color = R.color.grey)
        )
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

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> {
                finishAct()
            }
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
            binding.etDate -> {
                datePicker()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is ViewBookReturnDataResponse -> {
                        vm.viewReturnList.clear()
//                        "Response: viewBookReturn : ${apiRenderState.result}".logE()

                        if (apiRenderState.result.data?.jsonMemberReturn?.retutranlist?.isNullOrEmpty() == true) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.tvNoData.visibility = View.GONE

                            apiRenderState.result.data?.jsonMemberReturn?.retutranlist?.map {
                                if (it != null) {
                                    vm.viewReturnList.add(it)
                                }
                            }

                            rvUtil?.rvAdapter?.notifyDataSetChanged()
                        }
                        progressDialog.hideProgress()
                    }
                }
            }
            else -> {
                progressDialog.hideProgress()
            }
        }
    }
}