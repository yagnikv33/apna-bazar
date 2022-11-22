package com.esjayit.apnabazar.main.dashboard.view.stock_view

import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.GetReturnLisitngResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.databinding.FragmentStockViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class StockViewFrag : BaseFrag<FragmentStockViewBinding, StockViewVM>(Layouts.fragment_stock_view) {
    override val hasProgress: Boolean = false
    override val vm: StockViewVM by viewModel()
    val progressDialog: CustomProgress by lazy { CustomProgress(requireActivity()) }

    override fun init() {
        progressDialog?.showProgress()
        vm?.getReturnListData(userId = prefs.user.userId, installedId = prefs.installId!!)
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is GetReturnLisitngResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            successToast(apiRenderState.result.message.toString())
                            "Get 5% Return Data ${apiRenderState.result.data}".logE()
                        } else {
                            errorToast(apiRenderState.result.message.toString())
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
                "Error API CALLING API ERROR".logE()
            }
        }
    }
}