package com.esjayit.apnabazar.main.dashboard.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.esjayit.R
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.helper.util.logE
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

    override fun init() {
        bottomNav()
    }

    private fun switchToFragment(frag: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        manager.beginTransaction().replace(R.id.frag_container, frag).commit()
    }

    private fun bottomNav() {

        switchToFragment(HomeFrag())

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page -> {
                    // Respond to navigation HOME
                    switchToFragment(HomeFrag())
                    true
                }
                R.id.stock_view_page -> {
                    // Respond to navigation STOCK VIEW
                    switchToFragment(StockViewFrag())
                    true
                }
                R.id.demand_list_page -> {
                    // Respond to navigation DEMAND LIST
                    switchToFragment(DemandListFrag())
                    true
                }
                R.id.user_ledger_page -> {
                    // Respond to navigation USER LEDGER
                    switchToFragment(UserLedgerFrag())
                    true
                }
                R.id.profile_page -> {
                    // Respond to navigation PROFILE
                    switchToFragment(ProfileFrag())
                    true
                }
                else -> false
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}