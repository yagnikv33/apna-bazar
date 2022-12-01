package com.esjayit.apnabazar.main.dashboard.view

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.AppConstants.App.BundleData.ADD_DEMAND_CODE
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.apnabazar.main.dashboard.view.demand.DemandListFrag
import com.esjayit.apnabazar.main.dashboard.view.home.HomeFrag
import com.esjayit.apnabazar.main.dashboard.view.profile.ProfileFrag
import com.esjayit.apnabazar.main.dashboard.view.stock_view.StockViewFrag
import com.esjayit.apnabazar.main.dashboard.view.user_ledger.UserLedgerFrag
import com.esjayit.databinding.ActivityDashboardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardAct : BaseAct<ActivityDashboardBinding, DashboardVM>(Layouts.activity_dashboard) {

    override val vm: DashboardVM by viewModel()
    override val hasProgress: Boolean = false

    val homeFrag = HomeFrag()
    val stockView = StockViewFrag()
    val demandList = DemandListFrag()
    val userLedgerFrag = UserLedgerFrag()
    val profile = ProfileFrag()

    override fun init() {
        bottomNav()
    }

    private fun switchToFragment(frag: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        manager.beginTransaction().replace(R.id.frag_container, frag).commit()
    }

    private fun bottomNav() {

        switchToFragment(homeFrag)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page -> {
                    // Respond to navigation HOME
                    switchToFragment(homeFrag)
                    true
                }
                R.id.stock_view_page -> {
                    // Respond to navigation STOCK VIEW
                    switchToFragment(stockView)
                    true
                }
                R.id.demand_list_page -> {
                    // Respond to navigation DEMAND LIST
                    switchToFragment(demandList)
                    true
                }
                R.id.user_ledger_page -> {
                    // Respond to navigation USER LEDGER
                    switchToFragment(userLedgerFrag)
                    true
                }
                R.id.profile_page -> {
                    // Respond to navigation PROFILE
                    switchToFragment(profile)
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppConstants.App.BundleData.RETURN_LIST_CODE -> {
                supportFragmentManager.fragments.find {
                    it is StockViewFrag
                }?.onActivityResult(requestCode, resultCode, data)
            }
            ADD_DEMAND_CODE -> {
                if (resultCode === Activity.RESULT_OK) {
                    switchToFragment(demandList)
                    binding.bottomNavigation.selectedItemId = R.id.demand_list_page
                }
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId == R.id.home_page) {
            super.onBackPressed()
        } else {
            switchToFragment(DemandListFrag())
            binding.bottomNavigation.selectedItemId = R.id.home_page
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}