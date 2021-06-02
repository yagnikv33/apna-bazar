package com.yudiz.e_cigarette.main.homemodule.vaping

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.yudiz.databinding.ActivityPgVgBinding
import com.yudiz.e_cigarette.AppConstants
import com.yudiz.e_cigarette.AppConstants.Api.Value.TYPE_IMAGE
import com.yudiz.e_cigarette.AppConstants.Api.Value.TYPE_VIDEO
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.KnowFactsResponse
import com.yudiz.e_cigarette.data.model.response.PgVgResponse
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.HomeAct
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class PgVgAct : BaseAct<ActivityPgVgBinding, HomeVM>(Layouts.activity_pg_vg) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true

    private var player: SimpleExoPlayer? = null

    override fun init() {
        vm.getPgVgData()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.ivBack -> {
                startActivity(HomeAct::class.java,null,null,true)
                finish()
            }
        }
    }

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
            vm.pgVgData.value?.data?.advertisements?.ads.let { MediaItem.fromUri(it!!) }

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
            TYPE_VIDEO -> {
                binding.brandAdsPlayer.visibility = View.VISIBLE
                initializePlayer()
                initListeners()
            }
            TYPE_IMAGE -> {
                binding.ivAdsViewer.visibility = View.VISIBLE
            }
        }
    }

    private fun loadContent(content: String) {
        binding.webView.loadDataWithBaseURL(null, content, "text/html; charset=UTF-8", null, null)
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is PgVgResponse) {

                    loadContent(apiRenderState.result.data.pgVg.description)
                    formatType(apiRenderState.result.data.advertisements.type)
                }
                hideProgress()
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