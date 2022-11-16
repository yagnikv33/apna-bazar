package com.esjayit.apnabazar.main.dashboard.view.profile

import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.profile.model.ProfileVM
import com.esjayit.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFrag : BaseFrag<FragmentProfileBinding, ProfileVM>(Layouts.fragment_profile) {

    override val vm: ProfileVM by viewModel()
    override val hasProgress: Boolean = false

    override fun init() {

    }

    override fun renderState(apiRenderState: ApiRenderState) {

    }

}