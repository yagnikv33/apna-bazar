package com.yudiz.e_cigarette.main.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class BaseVpFragAdapter(fm: FragmentManager, var arr: List<Fragment>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = arr[position]

    override fun getCount() = arr.size
}





















