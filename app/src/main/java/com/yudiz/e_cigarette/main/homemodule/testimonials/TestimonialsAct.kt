package com.yudiz.e_cigarette.main.homemodule.testimonials

import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.yudiz.BR
import com.yudiz.databinding.ActivityTestimonialsBinding
import com.yudiz.e_cigarette.AppConstants
import com.yudiz.e_cigarette.Layouts
import com.yudiz.e_cigarette.data.model.response.TestimonialsItem
import com.yudiz.e_cigarette.data.model.response.TestimonialsResponse
import com.yudiz.e_cigarette.helper.util.logE
import com.yudiz.e_cigarette.main.base.BaseAct
import com.yudiz.e_cigarette.main.base.rv.BaseRvBindingAdapter
import com.yudiz.e_cigarette.main.common.ApiRenderState
import com.yudiz.e_cigarette.main.homemodule.model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestimonialsAct :
    BaseAct<ActivityTestimonialsBinding, HomeVM>(Layouts.activity_testimonials) {

    override val vm: HomeVM by viewModel()

    override val hasProgress: Boolean = true
    private var player: SimpleExoPlayer? = null
    private lateinit var testimonialsAdapter: BaseRvBindingAdapter<TestimonialsItem>

    override fun init() {
        vm.getTestimonialsData()
        setAdapter()
    }

    private fun setAdapter() {
        testimonialsAdapter = BaseRvBindingAdapter(
            Layouts.raw_testimonials,
            mutableListOf(),
            br = BR.data
        )
        binding.rvTestimonials.adapter = testimonialsAdapter
    }

    private fun showViews() {
        binding.tvTestimonials.visibility = View.VISIBLE
        binding.ivHome.visibility = View.VISIBLE

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
            vm.testimonialsData.value?.data?.ads.let { MediaItem.fromUri(it!!) }

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
                if (apiRenderState.result is TestimonialsResponse) {

                    apiRenderState.result.data?.testimonials?.let {
                        testimonialsAdapter.addData(
                            it,
                            isClear = true
                        )
                    }

                    formatType(apiRenderState.result.data?.type)
                    hideProgress()
                    showViews()
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