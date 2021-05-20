package com.skeletonkotlin.e_cigarette.main.entrymodule.view

import android.content.Intent
import android.graphics.Color
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.skeletonkotlin.databinding.ActivitySplashScreenBinding
import com.skeletonkotlin.e_cigarette.Layouts
import com.skeletonkotlin.e_cigarette.data.model.response.SplashResponse
import com.skeletonkotlin.e_cigarette.helper.util.logE
import com.skeletonkotlin.e_cigarette.main.base.BaseAct
import com.skeletonkotlin.e_cigarette.main.common.ApiRenderState
import com.skeletonkotlin.e_cigarette.main.entrymodule.model.EntryVM
import com.skeletonkotlin.e_cigarette.main.home.HomeAct
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenAct :
    BaseAct<ActivitySplashScreenBinding, EntryVM>(Layouts.activity_splash_screen) {

    var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override val vm: EntryVM by viewModel()

    override val hasProgress: Boolean = true

    override fun init() {
        vm.getSplashScreenData()
        initListeners()
        playerView = binding.playerView
        initializePlayer()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.tvToHome -> {
                startActivity(Intent(application, HomeAct::class.java))
                finish()
            }
        }
    }

    private fun initViews() {
        binding.tvToHome.text = vm.splashData.value?.data?.title

        vm.splashData.value?.data?.backgroundColor?.let {
            binding.tvToHome.setBackgroundColor(
                Color.parseColor(
                    it
                )
            )
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
        player?.volume = 0f

        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())

        player = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()

        playerView!!.player = player

        val mediaItem: MediaItem = vm.splashData.value?.data?.video.let { MediaItem.fromUri(it!!) }
        "Video: ${vm.splashData.value?.data?.video}"
        player!!.setMediaItem(mediaItem)

        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
            player?.stop()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
            player?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player?.playWhenReady!!
            playbackPosition = player?.currentPosition!!
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                if (apiRenderState.result is SplashResponse) {
                    initViews()
                    initializePlayer()
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