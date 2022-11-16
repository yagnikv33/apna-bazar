package com.esjayit.apnabazar.main.dashboard.view.stock_view

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.stock_view.model.StockViewVM
import com.esjayit.databinding.FragmentStockViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class StockViewFrag : BaseFrag<FragmentStockViewBinding, StockViewVM>(Layouts.fragment_stock_view) {

    override fun renderState(apiRenderState: ApiRenderState) {

    }

    override val hasProgress: Boolean = false
    override val vm: StockViewVM by viewModel()

    override fun init() {

    }
}