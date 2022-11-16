package com.esjayit.apnabazar.main.dashboard.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.esjayit.R
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseAct
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.model.DashboardVM
import com.esjayit.databinding.ActivityDashboardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardAct : BaseAct<ActivityDashboardBinding, DashboardVM>(Layouts.activity_dashboard) {

    override val vm: DashboardVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {

        bottomNav()
    }

    fun switchToFragment(frag: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        manager.beginTransaction().replace(R.id.frag_container, frag).commit()
    }

    private fun bottomNav() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.bill_page -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.search_page -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.history_page -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.account_page -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }
}