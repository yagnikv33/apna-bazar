package com.yudiz.e_cigarette.main.homemodule.vaping

import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.yudiz.BR
import com.yudiz.databinding.ActivityKnowTheFactsBinding
import com.yudiz.e_cigarette.AppConstants
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.KnowFactsResponse
import com.yudiz.e_cigarette.data.model.response.KnowTheFactsItem
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.HomeAct
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class KnowTheFactsAct :
    BaseAct<ActivityKnowTheFactsBinding, HomeVM>(Layouts.activity_know_the_facts) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true

    private var player: SimpleExoPlayer? = null

    private lateinit var factsAdapter: BaseRvBindingAdapter<KnowTheFactsItem>

    override fun init() {
        vm.getFactsList()
        setAdapter()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v){
            binding.ivHome -> {
                startActivity(HomeAct::class.java,null,null,true)
                finish()
            }
        }
    }

    private fun setAdapter() {
        factsAdapter = BaseRvBindingAdapter(
            Layouts.raw_know_the_fact,
            mutableListOf(),
            br = BR.item
        )
        binding.rvKnowFacts.adapter = factsAdapter
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

    private fun initializePlayer() {

        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())

        player = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()

        binding.brandAdsPlayer.player = player

        val mediaItem: MediaItem =
            vm.factsData.value?.data?.advertisements?.ads.let { MediaItem.fromUri(it!!) }

        player?.apply {
            repeatMode = Player.REPEAT_MODE_ALL
            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
            volume = 0f
        }
    }

    private fun formatType(type: String?) {
        when (type) {
            AppConstants.Api.Value.TYPE_VIDEO -> {
                binding.brandAdsPlayer.visibility = View.VISIBLE
                initializePlayer()
                initListeners()
            }
            AppConstants.Api.Value.TYPE_IMAGE -> {
                binding.ivAdsViewer.visibility = View.VISIBLE
            }
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is KnowFactsResponse) {

                    factsAdapter.addData(
                        apiRenderState.result.data?.knowTheFacts.orEmpty(),
                        isClear = true
                    )

                    formatType(apiRenderState.result.data?.advertisements?.type)
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