package com.esjayit.apnabazar.main.dashboard.view.demand


import android.app.AlertDialog
import android.view.View
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.demand.model.DemandListVM
import com.esjayit.databinding.ActivityAddDemandBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddDemandAct : BaseAct<ActivityAddDemandBinding, DemandListVM>(Layouts.activity_add_demand) {

    override val vm: DemandListVM by viewModel()
    override val hasProgress: Boolean = false

    var builder: AlertDialog.Builder? = null

    var sex = arrayOf("Male", "Female")


    override fun init() {
        //vm.getMediumList(prefs?)
        setDropDown()
    }

    private fun setDropDown() {
        builder = AlertDialog.Builder(this)
        builder?.setItems(sex) { dialog, which ->
            binding.etModule.setText(sex[which])
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.etModule -> {
                builder?.show()
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}