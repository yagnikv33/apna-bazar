package com.yudiz.e_cigarette.main.homemodule.personalise_vape

import android.view.View
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.yudiz.BR
import com.yudiz.databinding.ActivityPersonaliseVapeBinding
import com.yudiz.e_cigarette.AppConstants
import com.yudiz.e_cigarette.AppConstants.Api.Value.TYPE_GIF
import com.yudiz.e_cigarette.AppConstants.Api.Value.TYPE_VIDEO
import com.yudiz.e_cigarette.AppConstants.App.Buttons.CIGARETTES_PER_DAY
import com.yudiz.e_cigarette.AppConstants.App.Buttons.HABITS
import com.yudiz.e_cigarette.AppConstants.App.Buttons.YEARS_SMOKING
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.*
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.HomeAct
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonaliseVapeAct :
    BaseAct<ActivityPersonaliseVapeBinding, HomeVM>(Layouts.activity_personalise_vape) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean
        get() = false

    private var player: SimpleExoPlayer? = null
    private lateinit var smokingAdapter: BaseRvBindingAdapter<QuestionsItem>
    private lateinit var cigaretteAdapter: BaseRvBindingAdapter<QuestionsItem>

    override fun init() {
        vm.getPersonaliseVapeData()
        setSmokingAdapter()
        setCigaretteAdapter()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivHome -> {
                startActivity(HomeAct::class.java, null, null, true)
                finish()
            }
            binding.cardNext -> {

            }
        }
    }

    private fun rvSmokingClick(v: View, item: QuestionsItem, pos: Int) {
        "Response: Click ${item.options?.get(0)?.id}"
    }

    private fun setSmokingAdapter() {
        smokingAdapter = BaseRvBindingAdapter(
            Layouts.raw_smoking_buttons,
            mutableListOf(),
            clickListener = ::rvSmokingClick,
            brs = mapOf(BR.que_item to QuestionsItem(), BR.ope_item to OptionsItem())
        )
        binding.rvSmoking.adapter = smokingAdapter
    }

    private fun setCigaretteAdapter() {
        cigaretteAdapter = BaseRvBindingAdapter(
            Layouts.raw_cigarette_buttons,
            mutableListOf(),
            clickListener = ::rvSmokingClick,
            brs = mapOf(BR.que_item to QuestionsItem(), BR.ope_item to OptionsItem())
        )
        binding.rvCigarette.adapter = cigaretteAdapter
    }

    //exo player
    private fun initListeners() {
        player?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                "ExoPlayer ${error.message}".logE()
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
                "ExoPlayer $reason".logE()
            }
        })
    }

    private fun initializePlayer(url: String?) {

        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())

        player = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()

        binding.brandAdsPlayer.player = player

        val mediaItem: MediaItem =
            MediaItem.fromUri(url.orEmpty())

        player?.apply {
            repeatMode = Player.REPEAT_MODE_ALL
            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
            volume = 0f
        }
    }

    private fun formatType(type: String?, url: String?) {
        when (type) {
            TYPE_VIDEO -> {
                binding.brandAdsPlayer.visibility = View.VISIBLE
                initializePlayer(url)
                initListeners()
            }
            TYPE_GIF -> {
                binding.ivAdsViewer.visibility = View.VISIBLE
                Glide
                    .with(this)
                    .load(url)
                    .into(binding.ivAdsViewer)
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is PersonaliseVapeResponse) {

                    /*apiRenderState.result.data.questions?.forEach { it ->
                        when (it.slug) {
                            YEARS_SMOKING -> {
                                apiRenderState.result.data.questions.let {
                                    smokingAdapter.addData(
                                        it,
                                        isClear = true
                                    )
                                }
                                "Response: Smoking ${apiRenderState.result.data.questions}".logE()
                            }
                            CIGARETTES_PER_DAY -> {
                                apiRenderState.result.data.questions.let {
                                    cigaretteAdapter.addData(
                                        it,
                                        isClear = true
                                    )
                                }
                                "Response: Cigarette ${apiRenderState.result.data.questions}".logE()
                            }
                        }
                    }*/

                    apiRenderState.result.data.questions?.forEach { it ->
                        when (it.slug) {
                            YEARS_SMOKING -> {
                                //textview title
                                binding.tvSmokingTitle.text = it.question

                                apiRenderState.result.data.questions.let {
                                    smokingAdapter.addData(
                                        it,
                                        isClear = true
                                    )
                                }
                            }

                            CIGARETTES_PER_DAY -> {
                                //textview title
                                binding.tvCigaretteTitle.text = it.question

                                apiRenderState.result.data.questions.let {
                                    cigaretteAdapter.addData(
                                        it,
                                        isClear = true
                                    )
                                }
                            }
                        }
                        "Response: Success - ${apiRenderState.result.data.questions}".logE()
                    }

                    //Ads Displaying
                    apiRenderState.result.data.advertisements?.forEach {
                        when (it.slug) {
                            HABITS -> {
                                formatType(
                                    apiRenderState.result.data.advertisements[2].type,
                                    apiRenderState.result.data.advertisements[0].ads
                                )
                            }
                        }
                    }
                    hideProgress()
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
            }
            is ApiRenderState.ApiError<*> -> {
                hideProgress()
            }
        }
    }
}